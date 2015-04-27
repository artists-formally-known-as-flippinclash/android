package com.tir38.android.blastermind.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.andorid.core.MatchEnd;
import com.bignerdranch.blastermind.andorid.core.Player;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.tir38.android.blastermind.backend.DataManager;
import com.tir38.android.blastermind.event.FeedbackEvent;
import com.tir38.android.blastermind.event.MatchEndedEvent;
import com.tir38.android.blastermind.view.GuessRowView;
import com.tir38.android.blastermind.view.InputButton;
import com.tir38.android.blastermind.view.SubmitButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class GameFragment extends BaseFragment implements GameActivity.BackPressedCallback, BaseActivity.BrightnessCallbacks {

    private static final int REQUEST_EXIT_MATCH_DIALOG = 1;
    private static final int REQUEST_END_OF_GAME_DIALOG = 2;

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final String EXIT_MATCH_TAG = "GameFragment.EXIT_MATCH_TAG";
    private static final String TAG_END_OF_GAME_DIALOG = "GameFragment.TAG_END_OF_GAME_DIALOG";

    @InjectView(R.id.fragment_game_guesses_container)
    protected LinearLayout mGuessContainer;
    @InjectView(R.id.fragment_game_input_container)
    protected LinearLayout mInputContainer;

    @Inject
    protected DataManager mDataManager;

    private SubmitButton mSubmitButton;
    private int mCurrentTurn;
    private GuessRowView mCurrentGuessRow;
    private int mRowHeightPx;
    private int mGuessContainerHeightPx;
    private List<GuessRowView> mGuessRows;
    private Player mCurrentPlayer;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    public static Fragment newInstance() {
        return new GameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.inject(this, view);

        createRows();
        setupInputButtons();

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mDataManager.getCurrentMatchName());
        }

        ((BaseActivity) getActivity()).registerBrightnessCallbacks(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((BaseActivity) getActivity()).unregisterBrightnessCallbacks(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT_MATCH_DIALOG
                && resultCode == Activity.RESULT_OK) {

            // user clicked on one of the buttons, but which one?
            int whichButton = data.getExtras().getInt(MaterialDialogFragment.EXTRA_WHICH, 0);
            if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                getActivity().finish();
            }
        }

        if (requestCode == REQUEST_END_OF_GAME_DIALOG) {
            getActivity().finish();
        }
    }

    public void onEventMainThread(MatchEndedEvent matchEndedEvent) {

        MatchEnd matchEnd = matchEndedEvent.getMatchEnd();
        mCurrentPlayer = mDataManager.getCurrentPlayer();
        int winnerId = matchEnd.getWinnerId();
        String winnerName = matchEnd.getWinnerName();

        if (winnerId == -1) {
            // nobody won
            if (mDataManager.isCurrentMatchMultiplayer())  {
                displayEndOfGameDialog("You all lose.");
            } else {
                displayEndOfGameDialog("You lose.");
            }

        } else if (mCurrentPlayer.getId() == winnerId) {
            // you won
            mDataManager.getCurrentPlayer();
            String dialogText = String.format(getResources().getString(R.string.you_won), mCurrentPlayer.getName());
            displayEndOfGameDialog(dialogText);
        } else {
            // you lost; somebody else won
            String dialogText = String.format(getResources().getString(R.string.somebody_else_won), winnerName);
            displayEndOfGameDialog(dialogText);
        }
    }

    public void onEventMainThread(FeedbackEvent feedbackEvent) {
        Feedback feedback = feedbackEvent.getFeedback();
        handleFeedback(feedback);

        mSubmitButton.setState(SubmitButton.STATE.DISABLED);

        mCurrentGuessRow.setNotCurrent();

        mCurrentTurn++;
        if (mCurrentTurn >= Logic.guessLimit) {
            return; // don't respond to feedback on last turn
        }

        mCurrentGuessRow = mGuessRows.get(mCurrentTurn);
        mCurrentGuessRow.setCurrent();
    }

    private void createRows() {
        // we need to manually compute the height of a guess container
        // so that all guesses can fit on the screen and take up the whole screen
        // we can only do this after the view tree has been layed out

        mGuessContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGuessContainerHeightPx = mGuessContainer.getHeight();

                // now that we have the height of the container, only now can we create our first guess row
                mRowHeightPx = mGuessContainerHeightPx / Logic.guessLimit;
                populateEmptyRows();

                // cleanup by removing listener
                mGuessContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }

    private void populateEmptyRows() {
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

        mInputContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // add submit button
                setupSubmitButton();
                mInputContainer.addView(mSubmitButton);

                // get types in order
                for (int i = 0; i < TYPE.values().length; i++) {
                    for (final TYPE type : TYPE.values()) {
                        if (type.getPosition() == i) {
                            setupInputButton(type, i);
                        }
                    }
                }

                // cleanup by removing listener
                mInputContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * setup individual input (color) button
     *
     * @param type
     */
    private void setupInputButton(final TYPE type, int childPosition) {
        InputButton inputButton = new InputButton(getActivity());

        TypedArray typedArray = getResources().obtainTypedArray(R.array.guess_colors);
        int index = type.getPosition();
        int color = typedArray.getColor(index, 0);
        typedArray.recycle();
        inputButton.setColor(color);

        // manually compute width based on available space:
        int submitButtonWidth = (int) getResources().getDimension(R.dimen.submit_button_size);
        int totalWidth = mInputContainer.getWidth();
        int inputButtonWidth = (totalWidth - submitButtonWidth) / TYPE.values().length;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(inputButtonWidth, inputButtonWidth);
        inputButton.setLayoutParams(layoutParams);

        // setup click listener
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentGuessRow.setActivePegType(type);
                setStateOfSubmitButton();
            }
        });

        mInputContainer.addView(inputButton, childPosition);
    }

    private void setupSubmitButton() {
        mSubmitButton = new SubmitButton(getActivity());

        int size = (int) getResources().getDimension(R.dimen.submit_button_size);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        mSubmitButton.setLayoutParams(layoutParams);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guess guess = mCurrentGuessRow.getGuess();
                mDataManager.sendGuess(guess);
                mSubmitButton.setState(SubmitButton.STATE.PENDING);
            }
        });
    }

    private void setStateOfSubmitButton() {
        if (mCurrentGuessRow == null) { // setting state for the first time
            mSubmitButton.setState(SubmitButton.STATE.DISABLED);
            return;
        }

        // if all pegs have a type then and only then enable update button
        if (mCurrentGuessRow.areAllPegsSet()) {
            mSubmitButton.setState(SubmitButton.STATE.ENABLED);
        } else {
            mSubmitButton.setState(SubmitButton.STATE.DISABLED);
        }
    }

    private void displayEndOfGameDialog(String title) {
        DialogFragment dialogFragment = new MaterialDialogFragment.FragmentBuilder()
                .title(title)
                .positiveButtonResId(android.R.string.ok)
                .build();
        dialogFragment.setTargetFragment(this, REQUEST_END_OF_GAME_DIALOG);
        dialogFragment.show(getFragmentManager(), TAG_END_OF_GAME_DIALOG);
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialogFragment = new MaterialDialogFragment.FragmentBuilder()
                .titleResId(R.string.exit_match)
                .contentResId(R.string.leave_match_msg)
                .positiveButtonResId(R.string.leave)
                .negativeButtonResId(R.string.stay)
                .build();

        dialogFragment.setTargetFragment(this, REQUEST_EXIT_MATCH_DIALOG);
        dialogFragment.show(getActivity().getFragmentManager(), EXIT_MATCH_TAG);
    }

    @Override
    public void setBrightness(int brightness) {
        // set background color
        int backgroundColor;
        TypedArray typedArray = getResources().obtainTypedArray(R.array.background_brightness);
        backgroundColor = typedArray.getColor(brightness, 0);
        typedArray.recycle();

        mGuessContainer.setBackgroundColor(backgroundColor);
        mInputContainer.setBackgroundColor(backgroundColor);
    }

    @Override
    public boolean registerForEvents() {
        return true;
    }
}