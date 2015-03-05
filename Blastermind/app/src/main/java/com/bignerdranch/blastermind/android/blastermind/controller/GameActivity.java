package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;


public class GameActivity extends SingleFragmentActivity {

    private static final String EXTRA_PLAYER = "GameActivity.EXTRA_PLAYER";

    public static Intent newIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }
}


