package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMatchFragment extends Fragment {

    @InjectView(R.id.fragment_create_match_start_match_button)
    protected Button mStartMatchButton;
    @InjectView(R.id.fragment_create_match_name_edit_text)
    protected EditText mNameEditText;

    public static Fragment newInstance() {
        return new CreateMatchFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_match, container, false);
        ButterKnife.inject(view, getActivity());
        return view;
    }

    @OnClick(R.id.fragment_create_match_start_match_button)
    protected void onStartClicked() {
    }

}
