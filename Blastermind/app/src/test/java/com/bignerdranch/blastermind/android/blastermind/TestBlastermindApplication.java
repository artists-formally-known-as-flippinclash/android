package com.bignerdranch.blastermind.android.blastermind;

import dagger.ObjectGraph;

public class TestBlastermindApplication extends BlastermindApplication {

    public void setObjectGraph(ObjectGraph objectGraph) {
        mApplicationGraph = objectGraph;
    }
}
