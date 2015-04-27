package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import com.bignerdranch.blastermind.android.blastermind.R;


public class GameActivity extends SingleFragmentActivity {

    private static final String EXTRA_PLAYER = "GameActivity.EXTRA_PLAYER";

    public static Intent newIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        // get fragment and call its onBackPressed
        FragmentManager fragmentManager = getFragmentManager();
        BackPressedCallback fragment = (BackPressedCallback) fragmentManager.findFragmentById(R.id.fragment_container);
        fragment.onBackPressed();
    }

    public interface BackPressedCallback {
        public void onBackPressed();
    }
}


