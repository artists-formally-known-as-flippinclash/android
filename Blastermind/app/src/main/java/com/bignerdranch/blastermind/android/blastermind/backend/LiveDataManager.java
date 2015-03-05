package com.bignerdranch.blastermind.android.blastermind.backend;

import android.util.Log;

import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import de.greenrobot.event.EventBus;

public class LiveDataManager implements DataManager {

    private static final String APP_KEY = "a8dc613841aa8963a8a4";
    private static final String TAG = LiveDataManager.class.getSimpleName();
    private static final String CHANNEL_NAME = "game-us";

    @Override
    public void setupConnection() {
        setupPusher();
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
