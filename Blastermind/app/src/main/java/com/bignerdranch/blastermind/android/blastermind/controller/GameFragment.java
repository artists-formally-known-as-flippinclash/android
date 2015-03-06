package com.bignerdranch.blastermind.android.blastermind.controller;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final String TAG_WINNER_DIALOG = "MainFragment.TAG_WINNER_DIALOG";
    private static final String TAG_LOSER_DIALOG = "MainFragment.TAG_LOSER_DIALOG";

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
                createEmptyGuessForNextTurn();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    mGuessContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    mGuessContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        return view;
    }

    public void onEventMainThread(MatchEndedEvent matchEndedEvent) {
        Toast.makeText(getActivity(), "match ended", Toast.LENGTH_SHORT).show();
    }

    public void onEventMainThread(FeedbackEvent feedbackEvent) {
        Feedback feedback = feedbackEvent.getFeedback();
        mCurrentGuessRow.setFeedback(feedback);
        handleFeedback(feedback);
        createEmptyGuessForNextTurn();
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
                break;
        }
    }

    @OnClick(R.id.update_button)
    public void onUpdateClick() {
        Guess guess = mCurrentGuessRow.getGuess();
        mDataManager.sendGuess(guess);
    }

    private void createEmptyGuessForNextTurn() {
        if (mCurrentTurn > Logic.guessLimit - 1) {
            mUpdateButton.setEnabled(false);
            return;
        }

        mCurrentTurn++;
        // create new row and add it
        mCurrentGuessRow = new GuessRowView(getActivity());

        int rowPaddingDp = (int) getResources().getDimension(R.dimen.row_padding);
        int rowPaddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowPaddingDp, getResources().getDisplayMetrics());
        int mRowHeightMinusPaddingPx = mRowHeightPx - rowPaddingPx; // remove padding
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mRowHeightMinusPaddingPx);
        layoutParams.setMargins(0, rowPaddingDp, 0, rowPaddingDp);

        mCurrentGuessRow.setLayoutParams(layoutParams);

        // set height
        mCurrentGuessRow.setup(Logic.guessWidth, mRowHeightMinusPaddingPx);
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
        if (mCurrentGuessRow == null) { // setting state for the first time
            mUpdateButton.setEnabled(false);
            return;
        }

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
