package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;

import com.bignerdranch.blastermind.android.blastermind.BlastermindApplication;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BlastermindApplication) getActivity().getApplication()).inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}
