package com.tir38.android.blastermind;

import android.app.Application;

import com.tir38.android.blastermind.analytics.AnalyticsFunnel;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class BlastermindApplication extends Application {

    protected ObjectGraph mApplicationGraph;

    @Inject
    protected AnalyticsFunnel mAnalyticsFunnel;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDependencyInjection();
        setupAnalytics();
    }

    private void setupDependencyInjection() {
        mApplicationGraph = createObjectGraph();
        mApplicationGraph.inject(this);
    }

    private ObjectGraph createObjectGraph() {
        return ObjectGraph.create(new BlastermindModule(getApplicationContext()));
    }

    private void setupAnalytics() {
        if (!BuildConfig.DEBUG) {
            mAnalyticsFunnel.initialize();
        }
    }

    public final void inject(Object object) {
        mApplicationGraph.inject(object);
    }

}
