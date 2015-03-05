package com.bignerdranch.blastermind.android.blastermind.backend;

import android.util.Log;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.android.blastermind.backend.request.GuessBody;
import com.bignerdranch.blastermind.android.blastermind.backend.response.GuessResponse;
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

    @Override
    public void setupConnection() {
        setupPusher();
        setupRestAdapter();
    }

    @Override
    public void sendGuess(Guess guess) {
        int matchId = 123;
        GuessBody guessBody = GuessBody.buildBodyFromGuess(guess);
        mRestService.sendGuess(matchId, guessBody, new Callback<GuessResponse>() {
            @Override
            public void success(GuessResponse guessResponse, Response response) {
                Log.d(RETROFIT_TAG, "Success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(RETROFIT_TAG, "Failure: " + error.toString()
                        + "\naccessing: " + error.getUrl()
                        + "\n" + Arrays.toString(error.getStackTrace()));
            }
        });
    }

    private void setupRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_REST_URL)
                .build();

        mRestService = restAdapter.create(BlasterRestService.class);
    }

    private void setupPusher() {
        Pusher pusher = new Pusher(APP_KEY);
        pusher.connect(new ConnectionEventListener() {
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
        Channel channel = pusher.subscribe(CHANNEL_NAME);

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
}
