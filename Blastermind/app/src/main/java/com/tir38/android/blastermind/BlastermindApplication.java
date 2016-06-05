package com.tir38.android.blastermind;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

public class BlastermindApplication extends Application {

    protected ObjectGraph mApplicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        mApplicationGraph = createObjectGraph();
    }

    protected ObjectGraph createObjectGraph() {
        return ObjectGraph.create(new BlastermindModule(getApplicationContext()));
    }

    public final void inject(Object object) {
        mApplicationGraph.inject(object);
    }


}
