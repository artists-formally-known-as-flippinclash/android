package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;

import com.bignerdranch.blastermind.android.blastermind.BlastermindApplication;

public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BlastermindApplication) getActivity().getApplication()).inject(this);
    }
}
