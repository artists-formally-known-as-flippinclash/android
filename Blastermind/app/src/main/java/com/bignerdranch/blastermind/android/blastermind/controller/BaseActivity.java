package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.bignerdranch.blastermind.android.blastermind.utils.DialogUtils;

public abstract class BaseActivity extends Activity {


    private static final String PREF_SCREEN_BRIGHTNESS = "BaseActivity.PREF_SCREEN_BRIGHTNESS";
    private int mBrightness;

    @Override
    protected void onResume() {
        super.onResume();

        // pull brightness from SharedPrefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mBrightness = sharedPreferences.getInt(PREF_SCREEN_BRIGHTNESS, 50);
    }

    protected void showProgressDialog(String message) {
        DialogUtils.showLoadingDialog(getFragmentManager(), message);
    }

    protected void dismissProgressDialog() {
        DialogUtils.hideLoadingDialog(getFragmentManager());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                makeLessBright();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                makeMoreBright();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void makeLessBright() {
        if (mBrightness > 0) {
            mBrightness--;
            updateSharedPref();
        }
    }

    private void makeMoreBright() {
        if (mBrightness < 100) {
            mBrightness++;
            updateSharedPref();
        }
    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit()
                .putInt(PREF_SCREEN_BRIGHTNESS, mBrightness)
                .apply();
    }
}
