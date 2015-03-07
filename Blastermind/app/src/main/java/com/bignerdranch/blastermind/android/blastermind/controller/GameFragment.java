package com.bignerdranch.blastermind.android.blastermind.controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.andorid.core.MatchEnd;
import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class GameFragment extends BaseFragment implements GameActivity.BackPressedCallback {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final String TAG_WINNER_DIALOG = "MainFragment.TAG_WINNER_DIALOG";
    private static final String TAG_LOSER_DIALOG = "MainFragment.TAG_LOSER_DIALOG";
    private static final int REQUEST_EXIT_MATCH_DIALOG = 1;
    private static final String EXIT_MATCH_TAG = "GameFragment.EXIT_MATCH_TAG";
    private static final int REQUEST_END_OF_GAME_DIALOG = 2;

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
    private int mRowHeightPx;
    private int mGuessContainerHeightPx;
    private List<GuessRowView> mGuessRows;
    private Player mCurrentPlayer;

    public static Fragment newInstance() {
        return new GameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.inject(this, view);

        setupInputButtons();

        // TODO move to helper method
        mGuessContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the width and height
                mGuessContainerHeightPx = mGuessContainer.getHeight();

                // now that we have the height of the container, only now can we create our first guess row
                mRowHeightPx = mGuessContainerHeightPx / Logic.guessLimit;
                createRows();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    mGuessContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    mGuessContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mDataManager.getCurrentMatchName());
        }

        return view;
    }

    public void onEventMainThread(MatchEndedEvent matchEndedEvent) {

        MatchEnd matchEnd = matchEndedEvent.getMatchEnd();
        mCurrentPlayer = mDataManager.getCurrentPlayer();
        int winnerId = matchEnd.getWinnerId();
        String winnerName = matchEnd.getWinnerName();

        if (winnerId == -1) {
            // nobody won
            displayLoserDialog("You all lose!");
        } else if (mCurrentPlayer.getId() == winnerId) {
            // you won
            mDataManager.getCurrentPlayer();
            String dialogText = String.format(getResources().getString(R.string.you_won), mCurrentPlayer.getName());
            displayWinnerDialog(dialogText);
        } else {
            // you lost; somebody else won
            String dialogText = String.format(getResources().getString(R.string.somebody_else_won), winnerName);
            displayLoserDialog(dialogText);
        }
    }

    public void onEventMainThread(FeedbackEvent feedbackEvent) {
        Feedback feedback = feedbackEvent.getFeedback();
        handleFeedback(feedback);

        mCurrentGuessRow.setNotCurrent();

        mCurrentTurn++;
        if (mCurrentTurn >= Logic.guessLimit) {
            return; // don't respond to feedback on last turn
        }

        mCurrentGuessRow = mGuessRows.get(mCurrentTurn);
        mCurrentGuessRow.setCurrent();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT_MATCH_DIALOG && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }

        if (requestCode == REQUEST_END_OF_GAME_DIALOG) {
            getActivity().finish();
        }
    }

    @OnClick(R.id.update_button)
    public void onUpdateClick() {
        Guess guess = mCurrentGuessRow.getGuess();
        mDataManager.sendGuess(guess);
        mUpdateButton.setEnabled(false); // don't allow spamming
    }

    private void createRows() {
        mGuessRows = new ArrayList<>();

        for (int i = 0; i < Logic.guessLimit; i++) {
            GuessRowView rowView = setupSingleRow();
            rowView.setNotCurrent();
            mGuessRows.add(rowView);
            mGuessContainer.addView(rowView);
        }

        // set first row to current
        mCurrentGuessRow = mGuessRows.get(0);
        mCurrentGuessRow.setCurrent();
    }

    private void handleFeedback(Feedback feedback) {
        mCurrentGuessRow.setFeedback(feedback);
    }

    private GuessRowView setupSingleRow() {
        GuessRowView guessRow = new GuessRowView(getActivity());

        int rowPaddingDp = (int) getResources().getDimension(R.dimen.row_padding);
        int rowPaddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowPaddingDp, getResources().getDisplayMetrics());
        int mRowHeightMinusPaddingPx = mRowHeightPx - rowPaddingPx; // remove padding
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        layoutParams.setMargins(0, 0, 0, rowPaddingDp);
        guessRow.setLayoutParams(layoutParams);

        // set height
        guessRow.setup(Logic.guessWidth, mRowHeightMinusPaddingPx);
        return guessRow;
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
        if (mCurrentGuessRow == null) { // setting state for the first time
            mUpdateButton.setEnabled(false);
            return;
        }

        // if all pegs have a type then and only then enable update button
        mUpdateButton.setEnabled(mCurrentGuessRow.areAllPegsSet());
    }

    private void displayWinnerDialog(String title) {
        AlertDialogFragment alertDialog = new AlertDialogFragment.Builder()
                .setTitle(title)
                .setPositiveButtonResId(android.R.string.ok)
                .setViewResId(R.layout.view_winner_dialog)
                .build();
        alertDialog.setTargetFragment(this, REQUEST_END_OF_GAME_DIALOG);
        alertDialog.show(getFragmentManager(), TAG_WINNER_DIALOG);
    }

    private void displayLoserDialog(String title) {
        AlertDialogFragment alertDialog = new AlertDialogFragment.Builder()
                .setTitle(title)
                .setPositiveButtonResId(android.R.string.ok)
                .build();
        alertDialog.setTargetFragment(this, REQUEST_END_OF_GAME_DIALOG);
        alertDialog.show(getFragmentManager(), TAG_LOSER_DIALOG);
    }

    @Override
    public void onBackPressed() {
        // todo display dialog
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment.Builder()
                .setMessage("Do you want to leave the match?")
                .setTitle("Exit match")
                .setPositiveButtonResId(android.R.string.ok)
                .setNegativeButtonResId(R.string.cancel)
                .build();

        alertDialogFragment.setTargetFragment(this, REQUEST_EXIT_MATCH_DIALOG);
        alertDialogFragment.show(getActivity().getFragmentManager(), EXIT_MATCH_TAG);

        Log.d(TAG, "display dialog");
    }
}