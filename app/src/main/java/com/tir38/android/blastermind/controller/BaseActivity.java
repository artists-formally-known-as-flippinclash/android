package com.tir38.android.blastermind.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.tir38.android.blastermind.R;
import com.tir38.android.blastermind.utils.DialogUtils;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String PREF_SCREEN_BRIGHTNESS = "BaseActivity.PREF_SCREEN_BRIGHTNESS";

    private int mBrightness;
    private BrightnessCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mBrightness = preferences.getInt(PREF_SCREEN_BRIGHTNESS, 0);
    }

    protected void showProgressDialog(String message) {
        DialogUtils.showLoadingDialog(getSupportFragmentManager(), message);
    }

    protected void dismissProgressDialog() {
        DialogUtils.hideLoadingDialog(getSupportFragmentManager());
    }

    public void registerBrightnessCallbacks(BrightnessCallbacks callbacks) {
        mCallbacks = callbacks;
        mCallbacks.setBrightness(mBrightness);
    }

    public void unregisterBrightnessCallbacks(BrightnessCallbacks callbacks) {
        if (callbacks == mCallbacks) {
            mCallbacks = null;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // intercept ACTION_UP on volume keys so that we don't pass on to OS and hear volume adjusting "beep"
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * volume buttons control screen brightness
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_VOLUME_DOWN:
                if (mCallbacks != null && mBrightness > 0) { // if callbacks are registered and brightness is not already at lowest setting
                    mBrightness--;
                    mCallbacks.setBrightness(mBrightness);
                    updateSharedPref();

                }
                return true;

            case android.view.KeyEvent.KEYCODE_VOLUME_UP:
                int maxBrightness = getResources().getInteger(R.integer.max_brightness_setting);
                if (mCallbacks != null && mBrightness < maxBrightness) { // if callbacks are registered and brightness is not already at highest setting
                    mBrightness++;
                    mCallbacks.setBrightness(mBrightness);
                    updateSharedPref();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void updateSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit()
                .putInt(PREF_SCREEN_BRIGHTNESS, mBrightness)
                .apply();
    }

    /**
     * Changes in game brightness.
     * Any Fragment/Activity can decide what "brightness" means within the context of its own view.
     */
    public interface BrightnessCallbacks {
        /**
         * @param brightness int from 0 (less bright) to {res.values.constants.max_brightness} (more bright)
         */
        void setBrightness(int brightness);
    }
}
