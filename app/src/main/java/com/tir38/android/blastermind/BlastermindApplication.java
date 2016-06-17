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

    protected void setupDependencyInjection() {
        mApplicationGraph = createObjectGraph();
        mApplicationGraph.inject(this);
    }

    private ObjectGraph createObjectGraph() {
        return ObjectGraph.create(new BlastermindModule(getApplicationContext()));
    }

    public final void inject(Object object) {
        mApplicationGraph.inject(object);
    }

    protected void setupAnalytics() {
        mAnalyticsFunnel.initialize();
    }
}
