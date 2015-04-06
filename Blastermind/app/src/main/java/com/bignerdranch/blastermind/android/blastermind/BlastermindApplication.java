package com.bignerdranch.blastermind.android.blastermind;

import android.app.Application;

import dagger.ObjectGraph;

public class BlastermindApplication extends Application {

    protected ObjectGraph mApplicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationGraph = createObjectGraph();
    }

    protected ObjectGraph createObjectGraph() {
        return ObjectGraph.create(new BlastermindModule(getApplicationContext()));
    }

    public final void inject(Object object) {
        mApplicationGraph.inject(object);
    }


}
