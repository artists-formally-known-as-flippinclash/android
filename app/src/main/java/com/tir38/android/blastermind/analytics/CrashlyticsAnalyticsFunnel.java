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
    private boolean mInitialzed;

    public CrashlyticsAnalyticsFunnel(Context context) {
        this.context = context;
    }

    @Override
    public void initialize() {
        mInitialzed = true;

        Fabric.with(context, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG).build())
                .build());
    }

    @Override
    public void logException(Exception exception) {
        if (!mInitialzed) {
            return;
        }
        Crashlytics.logException(exception);
    }

    @Override
    public void logErrorMessage(String errorMessage) {
        if (!mInitialzed) {
            return;
        }
        Crashlytics.log(errorMessage);
    }

    @Override
    public void logThrowable(Throwable throwable) {
        if (!mInitialzed) {
            return;
        }
        Crashlytics.logException(throwable);
    }

    @Override
    public void trackEvent(String event) {
        if (!mInitialzed) {
            return;
        }
        Answers.getInstance().logCustom(new CustomEvent(event));
    }

    @Override
    public void trackPlayerStartedMatch(String playerName) {
        if (!mInitialzed) {
            return;
        }
        Answers.getInstance().logCustom(new CustomEvent("Player tried to start new game")
                .putCustomAttribute("PlayerName", playerName));
    }
}