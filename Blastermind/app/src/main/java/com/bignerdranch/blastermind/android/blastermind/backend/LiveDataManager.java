package com.bignerdranch.blastermind.android.blastermind.backend;

import android.util.Log;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.backend.request.GuessBody;
import com.bignerdranch.blastermind.android.blastermind.backend.request.PlayerBody;
import com.bignerdranch.blastermind.android.blastermind.backend.response.GuessResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.StartMatchResponse;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateFailedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateSuccessEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiveDataManager implements DataManager {

    private static final String APP_KEY = "a8dc613841aa8963a8a4";
    public static final String BASE_REST_URL = "http://private-2ec32-blastermind.apiary-mock.com";
    public static final String REQUEST_BIN = "http://requestb.in/ohyr14oh";
    private static final String TAG = LiveDataManager.class.getSimpleName();
    private static final String RETROFIT_TAG = "RETROFIT: ";
    private static final String CHANNEL_NAME = "game-us";
    private BlasterRestService mRestService;
    private Pusher mPusher;

    public LiveDataManager() {
        mPusher = new Pusher(APP_KEY);
        setupRestAdapter();
    }

    @Override
    public void startMatch(Player player) {
        PlayerBody playerBody = PlayerBody.mapPlayerToBody(player);
        mRestService.startMatch(playerBody, new Callback<StartMatchResponse>() {
            @Override
            public void success(StartMatchResponse startMatchResponse, Response response) {
                String channel = startMatchResponse.getChannel();
                // TODO do somethign with existing player names
                // List<String> existingPlayers = startMatchResponse.getExistingPlayers();
                subscribeToPusherChannel(channel);
                EventBus.getDefault().post(new MatchCreateSuccessEvent());
            }

            @Override
            public void failure(RetrofitError error) {
                handleRetrofitError(error);
                EventBus.getDefault().post(new MatchCreateFailedEvent());
            }
        });
    }

    @Override
    public void sendGuess(Guess guess) {
        // TODO get from somewhere
        int matchId = 123;
        int playerId = 321;
        GuessBody guessBody = GuessBody.mapGuessToBody(guess);
        mRestService.sendGuess(matchId, playerId, guessBody, new Callback<GuessResponse>() {
            @Override
            public void success(GuessResponse guessResponse, Response response) {
                Feedback feedback = GuessResponse.mapResponseToFeedback(guessResponse);
                EventBus.getDefault().post(new FeedbackEvent(feedback));
            }

            @Override
            public void failure(RetrofitError error) {
                handleRetrofitError(error);
            }
        });
    }

    private void setupRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_REST_URL)
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
                EventBus.getDefault().post(new MatchEndedEvent());
            }
        });
    }

    private void handleRetrofitError(RetrofitError error) {
        Log.e(RETROFIT_TAG, "Failure: " + error.toString()
                + "\naccessing: " + error.getUrl()
                + "\n" + Arrays.toString(error.getStackTrace()));
    }
}
