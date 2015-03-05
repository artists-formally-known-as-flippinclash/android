package com.bignerdranch.blastermind.android.blastermind.backend;

import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class PusherWebManager implements WebManager {

    private static final String APP_KEY = "a8dc613841aa8963a8a4";
    private static final String TAG = PusherWebManager.class.getSimpleName();
    private static final String CHANNEL_NAME = "game-us";

    private Pusher mPusher;

    @Override
    public void setupConnection() {
        mPusher = new Pusher(APP_KEY);
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


        // subscribe
        Channel channel = mPusher.subscribe(CHANNEL_NAME);

        // bind
        channel.bind("match-started", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                Log.d(TAG, "match started! data: " + data);
            }
        });

        channel.bind("match-ended", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                Log.d(TAG, "match ended! data: " + data);
            }
        });
    }
}
