package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;

import com.tir38.android.blastermind.BlastermindApplication;

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

    protected void showProgressDialog(String message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog(message);
        }
    }

    protected void dismissProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissProgressDialog();
        }
    }

    protected boolean registerForEvents() {
        return false;
    }
}
