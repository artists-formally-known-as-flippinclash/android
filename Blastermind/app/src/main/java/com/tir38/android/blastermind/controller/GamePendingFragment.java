package com.tir38.android.blastermind.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.tir38.android.blastermind.R;
import com.tir38.android.blastermind.backend.DataManager;
import com.tir38.android.blastermind.core.Player;
import com.tir38.android.blastermind.event.MatchCreateFailedEvent;
import com.tir38.android.blastermind.event.MatchCreateSuccessEvent;
import com.tir38.android.blastermind.event.MatchStartedEvent;

import javax.inject.Inject;

public class GamePendingFragment extends BaseFragment {

    private static final String ARG_PLAYER = "GamePendingFragment.ARG_PLAYER";
    private static final String TAG = GamePendingFragment.class.getSimpleName();

    @Inject
    protected DataManager mDataManager;

    private Player mPlayer;

    public static Fragment newInstance(Player player) {
        GamePendingFragment fragment = new GamePendingFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_PLAYER, player);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getArguments().getSerializable(ARG_PLAYER);
        if (mPlayer == null) {
            Crashlytics.logException(new Throwable("player is null"));
            Log.e(TAG, "player is null!");
            getActivity().finish();
        }

        // display loading dialog
        String message = getResources().getString(R.string.creating_match);
        showProgressDialog(message);

        // create game
        mDataManager.startMatch(mPlayer);
    }

    public void onEventMainThread(MatchCreateSuccessEvent matchCreateSuccessEvent) {
        dismissProgressDialog();
        String matchName = matchCreateSuccessEvent.getMatchName();
        // display loading dialog
        String dialogText = String.format(getResources().getString(R.string.waiting_for_other_players), matchName);
        showProgressDialog(dialogText);
    }

    public void onEventMainThread(MatchStartedEvent matchStartedEvent) {
        dismissProgressDialog();
        Intent intent = GameActivity.newIntent(getActivity());
        startActivity(intent);
    }

    public void onEventMainThread(MatchCreateFailedEvent event) {
        dismissProgressDialog();
        Toast.makeText(getActivity(), R.string.failed_to_create_match_msg, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public boolean registerForEvents() {
        return true;
    }
}

