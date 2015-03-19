package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;

import com.bignerdranch.blastermind.android.blastermind.BlastermindApplication;

import de.greenrobot.event.EventBus;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BlastermindApplication) getActivity().getApplication()).inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (registerForEvents()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registerForEvents()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean registerForEvents() {
        return false;
    }
}
