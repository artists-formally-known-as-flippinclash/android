package com.bignerdranch.blastermind.android.blastermind.helpers;

import com.bignerdranch.blastermind.android.blastermind.TestBlastermindApplication;

import org.robolectric.Robolectric;

import dagger.ObjectGraph;

public class ModuleHelper {

    private ModuleHelper() {
        // can't instantiate
    }

    /**
     * Allows for setting dagger test module for individual tests or test classes.
     * Instead of having one "MockModule" for all tests.
     *
     * @param module
     */
    public static void setModule(Object module) {
        ObjectGraph objectGraph = ObjectGraph.create(module);
        TestBlastermindApplication application = ((TestBlastermindApplication) Robolectric.application);
        application.setObjectGraph(objectGraph);
    }
}

