package com.tir38.android.blastermind.backend;

import android.util.Log;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.tir38.android.blastermind.BuildConfig;
import com.tir38.android.blastermind.analytics.AnalyticsFunnel;
import com.tir38.android.blastermind.backend.request.GuessBody;
import com.tir38.android.blastermind.backend.request.PlayerBody;
import com.tir38.android.blastermind.backend.response.GuessResponse;
import com.tir38.android.blastermind.backend.response.MatchEndResponse;
import com.tir38.android.blastermind.backend.response.NullResponse;
import com.tir38.android.blastermind.backend.response.StartMatchResponse;
import com.tir38.android.blastermind.core.Feedback;
import com.tir38.android.blastermind.core.Guess;
import com.tir38.android.blastermind.core.MatchEnd;
import com.tir38.android.blastermind.core.Player;
import com.tir38.android.blastermind.event.FeedbackEvent;
import com.tir38.android.blastermind.event.MatchCreateFailedEvent;
import com.tir38.android.blastermind.event.MatchCreateSuccessEvent;
import com.tir38.android.blastermind.event.MatchEndedEvent;
import com.tir38.android.blastermind.event.MatchStartedEvent;
import com.tir38.android.blastermind.event.NetworkFailureEvent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class LiveGameSupervisor implements GameSupervisor {

    private static final String TEST_BASE_REST_URL = "http://private-2ec32-blastermind.apiary-mock.com"; // testing
    private static final String BASE_REST_URL = "https://blastermind.herokuapp.com/"; // live

    private static final int MANUALLY_TRIGGER_MATCH_START_TIMEOUT = 15 * 1000; // fifteen seconds, in milliseconds

    private static final String TAG = LiveGameSupervisor.class.getSimpleName();
    private static final String RETROFIT_TAG = "RETROFIT: ";
    private static final String APP_KEY = "a8dc613841aa8963a8a4";

    private static final boolean USE_FAKE_WEB_SERVICES = false;

    private Pusher mPusher;
    private int mCurrentMatchId;
    private Player mPlayer;
    private String mCurrentMatchName;
    private List<String> mCurrentMatchPlayers; // includes mPlayer
    private BlastermindWebService mWebService;
    private AnalyticsFunnel mAnalyticsFunnel;

    public LiveGameSupervisor(AnalyticsFunnel analyticsFunnel) {
        mAnalyticsFunnel = analyticsFunnel;
        mPusher = new Pusher(APP_KEY);
        setupRestAdapter();
    }

    @Override
    public void startMatch(Player player) {
        mPlayer = player;
        PlayerBody playerBody = PlayerBody.mapPlayerToBody(player);

        Observable<StartMatchResponse> startMatchResponseObservable = mWebService.startMatch(playerBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        startMatchResponseObservable.subscribe(new Action1<StartMatchResponse>() {
            @Override
            public void call(StartMatchResponse startMatchResponse) {
                Log.d(RETROFIT_TAG, "my id: " + startMatchResponse.getMyId() + "; match name: " + startMatchResponse.getMatchName());
                mCurrentMatchId = startMatchResponse.getMatchId();
                mPlayer.setId(startMatchResponse.getMyId());
                mCurrentMatchName = startMatchResponse.getMatchName();

                mCurrentMatchPlayers = startMatchResponse.getExistingPlayers();

                // setup pusher
                String channel = startMatchResponse.getChannel();
                subscribeToPusherChannel(channel);
                EventBus.getDefault().post(new MatchCreateSuccessEvent(mCurrentMatchName));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                handleError(throwable);
                EventBus.getDefault().post(new MatchCreateFailedEvent());
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                manuallyTriggerMatchStart();
            }
        }, MANUALLY_TRIGGER_MATCH_START_TIMEOUT);
    }

    @Override
    public void sendGuess(Guess guess) {

        int playerId = mPlayer.getId();
        GuessBody guessBody = GuessBody.mapGuessToBody(guess);

        Observable<GuessResponse> guessResponseObservable = mWebService.sendGuess(mCurrentMatchId, playerId, guessBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        guessResponseObservable.subscribe(new Action1<GuessResponse>() {
            @Override
            public void call(GuessResponse guessResponse) {
                Log.d(RETROFIT_TAG, guessResponse.toString());
                Feedback feedback = GuessResponse.mapResponseToFeedback(guessResponse);

                EventBus.getDefault().post(new FeedbackEvent(feedback));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                EventBus.getDefault().post(new NetworkFailureEvent());
                handleError(throwable);
            }
        });
    }

    @Override
    public String getCurrentMatchName() {
        return mCurrentMatchName;
    }

    @Override
    public Player getCurrentPlayer() {
        return mPlayer;
    }

    @Override
    public boolean isCurrentMatchMultiplayer() {
        return mCurrentMatchPlayers.size() > 1;
    }

    private void setupRestAdapter() {
        String url;

        if (USE_FAKE_WEB_SERVICES && BuildConfig.DEBUG) {
            url = TEST_BASE_REST_URL;
        } else {
            url = BASE_REST_URL;
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }

        mWebService = builder.build().create(BlastermindWebService.class);
    }

    private void subscribeToPusherChannel(String channelName) {

        mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                // do nothing
            }

            @Override
            public void onError(String message, String code, Exception e) {
                mAnalyticsFunnel.logException(e);
                Log.e(TAG, "error: " + message + "; code: " + code);
            }
        }, ConnectionState.ALL);


        // subscribe to channel
        Log.d(TAG, "subscribing to channel: " + channelName);
        Channel channel = mPusher.subscribe(channelName);

        // bind to events
        channel.bind("match-started", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                EventBus.getDefault().post(new MatchStartedEvent());
            }
        });

        channel.bind("match-ended", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                mCurrentMatchId = -1; // reset match id
                MatchEnd matchEnd = parseMatchEndedJson(data);
                handleMatchEndEvent(matchEnd);
            }
        });
    }

    private void handleError(Throwable throwable) {
        mAnalyticsFunnel.logThrowable(throwable);
        Log.e(RETROFIT_TAG, "Failure: " + throwable.toString());
    }

    /**
     * Server isn't always sending Pusher notifications after 30 seconds to start a match
     * But there is an API endpoint to manually trigger the Pusher Notification ourselves
     */
    private void manuallyTriggerMatchStart() {
        Observable<NullResponse> nullResponseObservable = mWebService.triggerMatchStart(mCurrentMatchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        nullResponseObservable.subscribe(new Action1<NullResponse>() {
            @Override
            public void call(NullResponse nullResponse) {
                // do nothing;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                // do nothing
            }
        });
    }

    private MatchEnd parseMatchEndedJson(String json) {
        Gson gson = new Gson();
        MatchEndResponse response = gson.fromJson(json, MatchEndResponse.class);
        int id = response.getMatchId();
        int winnerId = response.getWinnerId();
        Guess solution = response.getSolution();
        String winnerName = response.getWinnerName();

        MatchEnd matchEnd = new MatchEnd();
        matchEnd.setMatchId(id);
        matchEnd.setWinnerId(winnerId);
        matchEnd.setWinnerName(winnerName);
        matchEnd.setSolution(solution);
        return matchEnd;
    }

    private void handleMatchEndEvent(MatchEnd matchEnd) {
        EventBus.getDefault().post(new MatchEndedEvent(matchEnd));
    }
}
