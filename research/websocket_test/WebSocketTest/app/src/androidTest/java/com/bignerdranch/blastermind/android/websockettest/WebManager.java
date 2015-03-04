package com.bignerdranch.blastermind.android.websockettest;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebManager {

    private static final String MOCK_URI = "ws://private-2ec32-blastermind.apiary-mock.com";
    private static final String TEST_URI = "ws://echo.websocket.org";

    private final String TAG = WebManager.class.getSimpleName();

    private WebSocketClient mWebSocketClient;

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(TEST_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i(TAG, "websocket opened");
                mWebSocketClient.send("Hello from jason!");
            }

            @Override
            public void onMessage(String message) {
                Log.i(TAG, "websocket message: " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i(TAG, "websocket closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i(TAG, "websocket error: " + ex.toString());
            }
        };
        mWebSocketClient.connect();
    }
}
