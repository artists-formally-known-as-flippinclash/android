package com.tir38.android.blastermind;

import com.tir38.android.blastermind.helpers.ModuleHelper;

import org.robolectric.RuntimeEnvironment;

public class BaseTest {

    public BaseTest() {
        if (usesInjection()) {
            ModuleHelper.setModule(getTestModule());
            ((BlastermindApplication) RuntimeEnvironment.application.getApplicationContext()).inject(this);
        }
    }

    protected boolean usesInjection() {
        return false;
    }

    /**
     * @return Object; must be annotated with Dagger's @Module annotation
     */
    protected Object getTestModule() {
        return null;
    }
}
