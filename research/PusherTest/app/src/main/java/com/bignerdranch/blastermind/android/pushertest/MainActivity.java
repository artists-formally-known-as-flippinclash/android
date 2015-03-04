package com.bignerdranch.blastermind.android.pushertest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;


public class MainActivity extends ActionBarActivity {

    private static final String APP_KEY = "c2403a68a67d600f9ed5";
    private static final String CHANNEL_NAME = "test_channel";
    private static final String TAG = MainActivity.class.getSimpleName();
    private Pusher mPusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectToPusher();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnect();
    }

    private void connectToPusher() {
        mPusher = new Pusher(APP_KEY);
        mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d(TAG, "state changed from " + change.getPreviousState() + " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.e(TAG, "error: " + message + "; code: " + code + "; exception: " + e);
            }
        }, ConnectionState.ALL);

        // subscribe
        Channel channel = mPusher.subscribe(CHANNEL_NAME);


//        // bind
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channelName, String eventName, String data) {
//                Log.d(TAG, "event: " + eventName + "; data: " + data);
//            }
//        });
    }

    private void disconnect () {
        if (mPusher != null) {
            mPusher.disconnect();

    }
}
