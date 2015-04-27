package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.bignerdranch.blastermind.android.blastermind.BuildConfig;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.tir38.android.blastermind.helpers.FragmentTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.tir38.android.blastermind.helpers.ActivityTestHelper.assertActivityStarted;
import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class CreateMatchFragmentTest {

    @InjectView(R.id.fragment_create_match_start_match_button)
    protected Button mStartButton;
    @InjectView((R.id.fragment_create_match_name_edit_text))
    protected EditText mNameEditText;

    private Fragment mFragment;

    @Before
    public void setup() throws Exception {
        mFragment = new CreateMatchFragment();
        FragmentTestHelper.startFragment(mFragment);
        ButterKnife.inject(this, mFragment.getView());
    }

    @Test
    public void emptyNameShouldDisableStartButton() throws Exception {
        mNameEditText.setText("");
        assertThat(mStartButton).isDisabled();
    }

    @Test
    public void enteringNameShouldEnableStartButton() throws Exception {
        mNameEditText.setText("asdf");
        assertThat(mStartButton).isEnabled();
    }

    @Test
    public void nameShouldNotExceed15Characters() throws Exception {
        mNameEditText.setText("asdfghjklqwertyu"); // 16 char string
        assertThat(mNameEditText).hasText("asdfghjklqwerty"); // 15 char string
    }

    @Test
    public void startButtonShouldStartGamePendingActivity() throws Exception {
        mNameEditText.setText("a");
        mStartButton.performClick();
        assertActivityStarted(mFragment.getActivity(), GamePendingActivity.class);
    }
}