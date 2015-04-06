package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.FragmentManager;
import android.content.Context;

import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.BaseTest;
import com.bignerdranch.blastermind.android.blastermind.CustomTestRunner;
import com.bignerdranch.blastermind.android.blastermind.DummyModule;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateFailedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchCreateSuccessEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.bignerdranch.blastermind.android.blastermind.helpers.FragmentTestHelper;
import com.bignerdranch.blastermind.android.blastermind.helpers.ToastTestHelper;
import com.bignerdranch.blastermind.android.blastermind.utils.DialogUtils;
import com.bignerdranch.blastermind.android.blastermind.utils.ProgressDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.bignerdranch.blastermind.android.blastermind.helpers.ActivityTestHelper.assertActivityStarted;
import static com.bignerdranch.blastermind.android.blastermind.helpers.ProgressDialogTestHelper.assertDialogFragmentHasProgressMessage;
import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@Config(emulateSdk = 18, reportSdk = 21)
@RunWith(CustomTestRunner.class)
public class GamePendingFragmentTest extends BaseTest {

    @Inject
    protected DataManager mDataManager;

    private GamePendingFragment mFragment;
    private Context mApplicationContext;

    @Before
    public void setup() throws Exception {
        mApplicationContext = Robolectric.getShadowApplication().getApplicationContext();
        Player player = new Player("alice");
        mFragment = (GamePendingFragment) GamePendingFragment.newInstance(player);
        FragmentTestHelper.startFragment(mFragment);
    }

    @Test
    public void onFragmentStarts_ShouldStartMatch() throws Exception {
        // check that DataManager.startMatch() was called with correct player name
        ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
        verify(mDataManager).startMatch(argument.capture());
        Player actualPlayer = argument.getValue();
        org.assertj.core.api.Assertions.assertThat(actualPlayer.getName()).isEqualTo("alice");
    }

    @Test
    public void onFragmentStart_ShouldShowCreatingMatchDialog() throws Exception {
        // has a progress dialog fragment
        FragmentManager fragmentManager = mFragment.getActivity().getFragmentManager();
        assertThat(fragmentManager).hasFragmentWithTag(DialogUtils.TAG_PROGRESS_DIALOG);

        // progress dialog has message
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(DialogUtils.TAG_PROGRESS_DIALOG);
        String expectedMessage = mApplicationContext.getResources().getString(R.string.creating_match);
        assertDialogFragmentHasProgressMessage(progressDialogFragment, expectedMessage);
    }

    @Test
    public void onMatchCreatedSuccess_ShouldShowWaitingForOtherPlayersDialog() throws Exception {
        String matchName = "expected match name";
        mFragment.onEventMainThread(new MatchCreateSuccessEvent(matchName));

        // has a progress dialog fragment
        FragmentManager fragmentManager = mFragment.getActivity().getFragmentManager();
        assertThat(fragmentManager).hasFragmentWithTag(DialogUtils.TAG_PROGRESS_DIALOG);

        // progress dialog has message
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(DialogUtils.TAG_PROGRESS_DIALOG);
        String expectedMessage = mApplicationContext.getResources().getString(R.string.waiting_for_other_players, matchName);
        assertDialogFragmentHasProgressMessage(progressDialogFragment, expectedMessage);
    }

    @Test
    public void onMatchCreateFailed_ShouldShowToast() throws Exception {
        mFragment.onEventMainThread(new MatchCreateFailedEvent());
        ToastTestHelper.assertThatNextToastIs("Failed to create game. Try again.");
    }

    @Test
    public void onMatchStarted_ShouldShowToast() throws Exception {
        mFragment.onEventMainThread(new MatchStartedEvent());
        ToastTestHelper.assertThatNextToastIs("Match started");
    }

    @Test
    public void onMatchStarted_ShouldStarGameActivity() throws Exception {
        mFragment.onEventMainThread(new MatchStartedEvent());
        assertActivityStarted(mFragment.getActivity(), GameActivity.class);
    }

    @Override
    protected boolean usesInjection() {
        return true;
    }

    @Override
    protected Object getTestModule() {
        return new MicroModule();
    }

    @Module(
            includes = DummyModule.class,
            injects = {GamePendingFragmentTest.class,
                    GamePendingFragment.class},
            overrides = true,
            library = true
    )
    protected static class MicroModule {

        @Provides
        @Singleton
        protected DataManager provideDataManager() {
            return Mockito.mock(DataManager.class);
        }
    }
}