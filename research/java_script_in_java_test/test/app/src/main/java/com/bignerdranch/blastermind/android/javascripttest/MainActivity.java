package com.bignerdranch.blastermind.android.javascripttest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup webview and javascript interface
        final WebView webView = (WebView) findViewById(R.id.empty_webview);
        MyJavascriptInterface javascriptInterface = new MyJavascriptInterface(this);
        webView.addJavascriptInterface(javascriptInterface, "AndroidTagName");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://tir38.com/temp/scripttest.html");

        // setup button
        Button fireButton = (Button) findViewById(R.id.fire_button);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:say_hello()");
            }
        });
    }

    public class MyJavascriptInterface {
        Context mContext;

        public MyJavascriptInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void responseSayHello(String response) {
            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
        }
    }
}


