package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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
        ButterKnife.inject(this, view);
        mStartMatchButton.setEnabled(false);
        return view;
    }

    @OnTextChanged(R.id.fragment_create_match_name_edit_text)
    public void onNameTextChanged() {
        validate();
    }

    @OnClick(R.id.fragment_create_match_start_match_button)
    public void onStartClicked() {
        String name = String.valueOf(mNameEditText.getText());
        Player player = new Player(name);
        Intent intent = GamePendingActivity.newIntent(getActivity(), player);
        startActivity(intent);
    }

    private void validate() {
        boolean isNameValid = !TextUtils.isEmpty(mNameEditText.getText());
        mStartMatchButton.setEnabled(isNameValid);
    }

}
