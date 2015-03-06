package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;

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


}
