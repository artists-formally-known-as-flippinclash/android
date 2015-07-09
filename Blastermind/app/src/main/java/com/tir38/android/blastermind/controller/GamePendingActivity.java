package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.tir38.android.blastermind.core.Player;

public class GamePendingActivity extends SingleFragmentActivity {

    private static final String EXTRA_PLAYER = "GamePendingActivity.EXTRA_PLAYER";


    public static Intent newIntent(Context context, Player player) {
        Intent intent = new Intent(context, GamePendingActivity.class);
        intent.putExtra(EXTRA_PLAYER, player);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Player player = (Player) getIntent().getSerializableExtra(EXTRA_PLAYER);
        return GamePendingFragment.newInstance(player);
    }
}
