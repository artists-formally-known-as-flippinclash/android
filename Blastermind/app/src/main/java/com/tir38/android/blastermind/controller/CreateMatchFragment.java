package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.tir38.android.blastermind.R;
import com.tir38.android.blastermind.core.Player;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CreateMatchFragment extends BaseFragment {

    private static final String PREF_PLAYER_NAME = "CreateMatchFragment.PREF_PLAYER_NAME";

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

        String name = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(PREF_PLAYER_NAME, null);
        mNameEditText.setText(name);

        return view;
    }

    @OnTextChanged(R.id.fragment_create_match_name_edit_text)
    public void onNameTextChanged() {
        validate();
    }

    @OnClick(R.id.fragment_create_match_start_match_button)
    public void onStartClicked() {
        String name = String.valueOf(mNameEditText.getText());
        savePlayerName(name);

        Player player = new Player(name);

        Answers.getInstance().logCustom(new CustomEvent("Player tried to start new game")
                .putCustomAttribute("PlayerName", name));

        Intent intent = GamePendingActivity.newIntent(getActivity(), player);
        startActivity(intent);
    }

    private void savePlayerName(String name) {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putString(PREF_PLAYER_NAME, name)
                .commit();
    }

    private void validate() {
        boolean isNameValid = !TextUtils.isEmpty(mNameEditText.getText());
        mStartMatchButton.setEnabled(isNameValid);
    }
}
