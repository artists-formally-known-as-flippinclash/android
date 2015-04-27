package com.tir38.android.blastermind.helpers;


import com.tir38.android.blastermind.TestBlastermindApplication;

import org.robolectric.RuntimeEnvironment;

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
        TestBlastermindApplication application = ((TestBlastermindApplication) RuntimeEnvironment.application);
        application.setObjectGraph(objectGraph);
    }
}

