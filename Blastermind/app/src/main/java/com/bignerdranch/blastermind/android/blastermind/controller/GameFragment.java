package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.backend.LiveDataManager;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final String TAG_WINNER_DIALOG = "MainFragment.TAG_WINNER_DIALOG";
    private static final String TAG_LOSER_DIALOG = "MainFragment.TAG_LOSER_DIALOG";
    private static final String ARG_PLAYER = "GameFragment.ARG_PLAYER";

    @InjectView(R.id.update_button)
    protected Button mUpdateButton;
    @InjectView(R.id.fragment_main_guesses_container)
    protected LinearLayout mGuessContainer;
    @InjectView(R.id.fragment_main_input_container)
    protected LinearLayout mInputContainer;

    @Inject
    protected DataManager mDataManager;

    private int mCurrentTurn;
    private GuessRowView mCurrentGuessRow;
    private int mMatchId = 123;
    private Player mPlayer;

    public static Fragment newInstance(Player player) {

        GameFragment gameFragment = new GameFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_PLAYER, player);
        gameFragment.setArguments(arguments);
        return gameFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getArguments().getSerializable(ARG_PLAYER);
        if (mPlayer == null) {
            Log.e(TAG, "player is null!");
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        createEmptyGuessForNextTurn();
        setupInputButtons();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mDataManager = new LiveDataManager();
        mDataManager.setupConnection();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(MatchStartedEvent matchStartedEvent) {
        Toast.makeText(getActivity(), "match started", Toast.LENGTH_SHORT).show();
    }

    public void onEventMainThread(MatchEndedEvent matchEndedEvent) {
        Toast.makeText(getActivity(), "match ended", Toast.LENGTH_SHORT).show();
    }

    public void onEventMainThread(FeedbackEvent feedbackEvent) {
        Feedback feedback = feedbackEvent.getFeedback();
        int matchId = feedback.getMatchId();
        if (matchId == mMatchId) { // if this feedback is about this match
            handleFeedback(feedback);
        }
    }

    private void handleFeedback(Feedback feedback) {
        String outcome = feedback.getOutcome();
        switch (outcome) {
            case "winner":
                displayWinnerDialog();
                break;
            case "loser":
                displayLoserDialog();
                break;
            default:
                String toastText = "positions correct: " + feedback.getPositionCount() + "; color correct: " + feedback.getColorCount();
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @OnClick(R.id.update_button)
    public void onUpdateClick() {
        Guess guess = mCurrentGuessRow.getGuess();
        mDataManager.sendGuess(guess);

        createEmptyGuessForNextTurn();
    }

    private void createEmptyGuessForNextTurn() {
        if (mCurrentTurn > Logic.guessLimit) {
            mUpdateButton.setEnabled(false);
        }
        mCurrentTurn++;

        // create new row and add it
        mCurrentGuessRow = new GuessRowView(getActivity());
        mCurrentGuessRow.setup(Logic.guessWidth);
        mGuessContainer.addView(mCurrentGuessRow);
    }

    private void setupInputButtons() {
        // get types in order
        for (int i = 0; i < TYPE.values().length; i++) {
            for (final TYPE type : TYPE.values()) {
                if (type.getPosition() == i) {
                    setupInputButton(type);
                }
            }
        }

        setStateOfUpdateButton();
    }

    private void setupInputButton(final TYPE type) {
        InputButton inputButton = new InputButton(getActivity());
        inputButton.setColor(type.getRgb());

        // set layout params
        int size = (int) getResources().getDimension(R.dimen.input_button_size);
        int padding = (int) getResources().getDimension(R.dimen.input_button_padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        layoutParams.setMargins(padding, padding, padding, padding);
        inputButton.setLayoutParams(layoutParams);

        // setup click listener
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentGuessRow.setActivePegType(type);
                setStateOfUpdateButton();

            }
        });

        mInputContainer.addView(inputButton);
    }

    private void setStateOfUpdateButton() {
        // if all pegs have a type then and only then enable update button
        mUpdateButton.setEnabled(mCurrentGuessRow.areAllPegsSet());
    }

    private void displayWinnerDialog() {
        AlertDialogFragment alertDialog = new AlertDialogFragment.Builder()
                .setTitleResId(R.string.you_won)
                .setPositiveButtonResId(android.R.string.ok)
                .setViewResId(R.layout.view_winner_dialog)
                .build();
        alertDialog.show(getFragmentManager(), TAG_WINNER_DIALOG);
    }

    private void displayLoserDialog() {
        AlertDialogFragment alertDialog = new AlertDialogFragment.Builder()
                .setTitleResId(R.string.you_lost)
                .setPositiveButtonResId(android.R.string.ok)
                .build();
        alertDialog.show(getFragmentManager(), TAG_LOSER_DIALOG);
    }

}
