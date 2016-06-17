package com.tir38.android.blastermind.analytics;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.BuildConfig;
import io.fabric.sdk.android.Fabric;

public final class CrashlyticsAnalyticsFunnel implements AnalyticsFunnel {

    private Context context;

    public CrashlyticsAnalyticsFunnel(Context context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        Fabric.with(context, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG).build())
                .build());
    }

    @Override
    public void logException(Exception exception) {
        Crashlytics.logException(exception);
    }

    @Override
    public void logErrorMessage(String errorMessage) {
        Crashlytics.log(errorMessage);
    }

    @Override
    public void trackEvent(String event) {
        Answers.getInstance().logCustom(new CustomEvent(event));
    }

    @Override
    public void trackPlayerStartedMatch(String playerName) {
        Answers.getInstance().logCustom(new CustomEvent("Player tried to start new game")
                .putCustomAttribute("PlayerName", playerName));
    }
}