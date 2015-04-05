package com.bignerdranch.blastermind.android.blastermind.controller;


import com.bignerdranch.blastermind.android.blastermind.CustomTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, reportSdk = 21)
public class SetupTest {

    // This test isn't really testing anything.
    // So it should always pass, unless something is broken with our Robolectric setup.
    @Test
    public void testRobolectricSetup() throws Exception {
        CreateMatchActivity activity = Robolectric.buildActivity(CreateMatchActivity.class).create().get();
        assertThat(activity != null);
    }
}
