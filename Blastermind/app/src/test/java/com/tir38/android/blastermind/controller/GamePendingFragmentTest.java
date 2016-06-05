package com.tir38.android.blastermind.controller;

import android.app.FragmentManager;
import android.content.Context;

import com.tir38.android.blastermind.BaseTest;
import com.tir38.android.blastermind.BuildConfig;
import com.tir38.android.blastermind.DummyModule;
import com.tir38.android.blastermind.R;
import com.tir38.android.blastermind.backend.DataManager;
import com.tir38.android.blastermind.core.Player;
import com.tir38.android.blastermind.event.MatchCreateFailedEvent;
import com.tir38.android.blastermind.event.MatchCreateSuccessEvent;
import com.tir38.android.blastermind.event.MatchStartedEvent;
import com.tir38.android.blastermind.helpers.FragmentTestHelper;
import com.tir38.android.blastermind.helpers.ToastTestHelper;
import com.tir38.android.blastermind.utils.DialogUtils;
import com.tir38.android.blastermind.utils.ProgressDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.tir38.android.blastermind.helpers.ActivityTestHelper.assertActivityStarted;
import static com.tir38.android.blastermind.helpers.ProgressDialogTestHelper.assertDialogFragmentHasProgressMessage;
import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GamePendingFragmentTest extends BaseTest {

    @Inject
    protected DataManager mDataManager;

    private GamePendingFragment mFragment;
    private Context mApplicationContext;

    @Before
    public void setup() throws Exception {
        mApplicationContext = RuntimeEnvironment.application.getApplicationContext();
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

//    // TODO looks like this test was accidentally added and the work never completed
//    @Test
//    public void onMultiplayerMatchStarted_ShouldShowPlayersNames() throws Exception {
//        List<String> currentPlayers = new ArrayList<>();
//        currentPlayers.add("Adam");
//        currentPlayers.add("Beth");
//        currentPlayers.add("Carl");
//        currentPlayers.add("Diane");
//
//        Mockito.when(mDataManager.isCurrentMatchMultiplayer()).thenReturn(true);
//        Mockito.when(mDataManager.getCurrentMatcpPlayers()).thenReturn(currentPlayers);
//
//        mFragment.onEventMainThread(new MatchStartedEvent());
//
//        ToastTestHelper.assertThatNextToastIs("Match started: Adam, Beth, Carl, Diane");
//    }

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