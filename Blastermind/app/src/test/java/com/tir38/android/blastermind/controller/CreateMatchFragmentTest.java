package com.tir38.android.blastermind.controller;

import android.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.tir38.android.blastermind.BaseTest;
import com.tir38.android.blastermind.BuildConfig;
import com.tir38.android.blastermind.DummyModule;
import com.tir38.android.blastermind.R;
import com.tir38.android.blastermind.analytics.AnalyticsFunnel;
import com.tir38.android.blastermind.helpers.FragmentTestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Module;
import dagger.Provides;

import static com.tir38.android.blastermind.helpers.ActivityTestHelper.assertActivityStarted;
import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CreateMatchFragmentTest extends BaseTest {

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


    @Override
    protected boolean usesInjection() {
        return true;
    }

    @Override
    protected Object getTestModule() {
        return new CreateMatchFragmentTest.MicroModule();
    }

    @Module(
            includes = DummyModule.class,
            injects = {CreateMatchFragmentTest.class,
                    CreateMatchFragment.class},
            overrides = true,
            library = true
    )
    protected static class MicroModule {

        @Provides
        @Singleton
        protected AnalyticsFunnel provideAnalyticsFunnel() {
            return Mockito.mock(AnalyticsFunnel.class);
        }
    }
}