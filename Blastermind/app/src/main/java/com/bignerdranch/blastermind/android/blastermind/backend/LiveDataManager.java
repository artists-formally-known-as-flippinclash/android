package com.bignerdranch.blastermind.android.blastermind.backend;

import android.util.Log;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.MatchEnd;
import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.backend.request.GuessBody;
import com.bignerdranch.blastermind.android.blastermind.backend.request.PlayerBody;
import com.bignerdranch.blastermind.android.blastermind.backend.response.GuessResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.MatchEndResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.NullResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.RoundEndResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.StartMatchResponse;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateFailedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateSuccessEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiveDataManager implements DataManager {

    private static final String APP_KEY = "a8dc613841aa8963a8a4";

    private static final boolean DEBUG = false;

    public static final String TEST_BASE_REST_URL = "http://private-2ec32-blastermind.apiary-mock.com"; // testing
    public static final String BASE_REST_URL = "http://api.blasterminds.com/"; // live

    private static final int MANUALLY_TRIGGER_MATCH_START_TIMEOUT = 15 * 1000; // fifteen seconds, in milliseconds
    private static final String TAG = LiveDataManager.class.getSimpleName();
    private static final String RETROFIT_TAG = "RETROFIT: ";

    private BlasterRestService mRestService;
    private Pusher mPusher;
    private int mCurrentMatchId;
    private Player mPlayer;
    private String mCurrentMatchName;

    public LiveDataManager() {
        mPusher = new Pusher(APP_KEY);
        setupRestAdapter();
    }

    @Override
    public void startMatch(Player player) {
        mPlayer = player;
        PlayerBody playerBody = PlayerBody.mapPlayerToBody(player);
        mRestService.startMatch(playerBody, new Callback<StartMatchResponse>() {
            @Override
            public void success(StartMatchResponse startMatchResponse, Response response) {
                Log.d(RETROFIT_TAG, "my id: " + startMatchResponse.getMyId()
                        + "; match name: " + startMatchResponse.getMatchName());
                mCurrentMatchId = startMatchResponse.getMatchId();
                mPlayer.setId(startMatchResponse.getMyId());
                mCurrentMatchName = startMatchResponse.getMatchName();

                // TODO do something with existing player names
                // List<String> existingPlayers = startMatchResponse.getExistingPlayers();

                // setup pusher
                String channel = startMatchResponse.getChannel();
                subscribeToPusherChannel(channel);
                EventBus.getDefault().post(new MatchCreateSuccessEvent(mCurrentMatchName));
            }

            @Override
            public void failure(RetrofitError error) {
                handleRetrofitError(error);
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
        mRestService.sendGuess(mCurrentMatchId, playerId, guessBody, new Callback<GuessResponse>() {
            @Override
            public void success(GuessResponse guessResponse, Response response) {
                Log.d(RETROFIT_TAG, guessResponse.toString());
                Feedback feedback = GuessResponse.mapResponseToFeedback(guessResponse);


                EventBus.getDefault().post(new FeedbackEvent(feedback));
            }

            @Override
            public void failure(RetrofitError error) {
                handleRetrofitError(error);
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

    private void setupRestAdapter() {
        String url;
        if (DEBUG) {
            url = TEST_BASE_REST_URL;
        } else {
            url = BASE_REST_URL;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .build();

        mRestService = restAdapter.create(BlasterRestService.class);
    }

    private void subscribeToPusherChannel(String channelName) {

        mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d(TAG, "state changed from :" + change.getPreviousState() + " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
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
                EventBus.getDefault().post(new MatchEndedEvent(matchEnd));
            }
        });

        channel.bind("round-ended", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                parseRoundEndJson(data);
            }
        });


        if (DEBUG) { // immediately start game
            EventBus.getDefault().post(new MatchStartedEvent());
        }
    }

    private void handleRetrofitError(RetrofitError error) {
        Log.e(RETROFIT_TAG, "Failure: " + error.toString() + " accessing: " + error.getUrl());
        error.printStackTrace();
    }

    /**
     * Server isn't always sending Pusher notifications after 30 seconds to start a match
     * But there is an API endpoint to manually trigger the Pusher Notification ourselves
     */
    private void manuallyTriggerMatchStart() {
        mRestService.triggerMatchStart(mCurrentMatchId, new Callback<NullResponse>() {
            @Override
            public void success(NullResponse nullResponse, Response response) {
                // we don't care
            }

            @Override
            public void failure(RetrofitError error) {
                // we don't care
            }
        });
    }

    private void parseRoundEndJson(String json) {
        Gson gson = new Gson();
        RoundEndResponse response = gson.fromJson(json, RoundEndResponse.class);
        int id = response.getMatchId();
        Guess solution = response.getSolution();
        int winnerId = response.getWinnerId();
        String winnerName = response.getWinnerName();
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
}
