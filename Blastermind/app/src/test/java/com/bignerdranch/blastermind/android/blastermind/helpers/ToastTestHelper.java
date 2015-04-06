package com.bignerdranch.blastermind.android.blastermind.helpers;

import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;

public class ToastTestHelper {

    public static void assertThatNextToastIs(String expectedToast) {
        assertEquals(expectedToast, ShadowToast.getTextOfLatestToast());
    }
}