package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateFailedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;

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
            Log.e(TAG, "player is null!");
            getActivity().finish();
        }

        // display loading dialog
        ((SingleFragmentActivity) getActivity()).showProgressDialog(R.string.waiting_for_other_players);

        // create game
        mDataManager.startMatch(mPlayer);
    }

    public void onEventMainThread(MatchStartedEvent matchStartedEvent) {
        dismissProgressDialog();
        Toast.makeText(getActivity(), "match started", Toast.LENGTH_SHORT).show();
        Intent intent = GameActivity.newIntent(getActivity());
        startActivity(intent);
    }

    public void onEventMainThread(MatchCreateFailedEvent event) {
        dismissProgressDialog();
        Toast.makeText(getActivity(), R.string.failed_to_create_match_msg, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    // TODO move to base fragment
    private void dismissProgressDialog() {
        ((SingleFragmentActivity) getActivity()).dismissProgressDialog();
    }
}

