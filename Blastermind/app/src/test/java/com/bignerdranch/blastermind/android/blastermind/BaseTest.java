package com.bignerdranch.blastermind.android.blastermind;

import com.bignerdranch.blastermind.android.blastermind.helpers.ModuleHelper;

import org.robolectric.Robolectric;

public class BaseTest {

    public BaseTest() {
        if (usesInjection()) {
            ModuleHelper.setModule(getTestModule());
            ((BlastermindApplication) Robolectric.getShadowApplication().getApplicationContext()).inject(this);
        }
    }

    protected boolean usesInjection() {
        return false;
    }

    /**
     *
     * @return Object; must be annotated with Dagger's @Module annotation
     */
    protected Object getTestModule() {
        return null;
    }
}
