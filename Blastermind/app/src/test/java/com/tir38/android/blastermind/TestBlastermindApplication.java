package com.tir38.android.blastermind;

import dagger.ObjectGraph;

public class TestBlastermindApplication extends BlastermindApplication {

    public void setObjectGraph(ObjectGraph objectGraph) {
        mApplicationGraph = objectGraph;
        objectGraph.inject(this);
    }

    @Override
    protected void setupDependencyInjection() {
        // do nothing
    }

    @Override
    protected void setupAnalytics() {
        // do nothing
    }
}
