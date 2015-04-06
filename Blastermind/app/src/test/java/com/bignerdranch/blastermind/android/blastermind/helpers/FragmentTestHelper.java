package com.bignerdranch.blastermind.android.blastermind.helpers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import com.bignerdranch.blastermind.android.blastermind.controller.TestBaseActivity;

public class FragmentTestHelper {

    public static void startFragment(Fragment fragment) {
        Activity activity = ActivityTestHelper.createAndStartActivity(TestBaseActivity.class);
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().add(fragment, null).commit();
    }
}
