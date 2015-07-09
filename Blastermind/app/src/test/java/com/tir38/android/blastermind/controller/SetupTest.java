package com.tir38.android.blastermind.controller;


import com.tir38.android.blastermind.BuildConfig;
import com.tir38.android.blastermind.TestBaseActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SetupTest {

    // This test isn't really testing anything.
    // So it should always pass, unless something is broken with our Robolectric setup.
    @Test
    public void testRobolectricSetup() throws Exception {
        TestBaseActivity activity = Robolectric.buildActivity(TestBaseActivity.class).create().get();
        assertThat(activity != null);
    }
}
