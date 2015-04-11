package com.bignerdranch.blastermind.android.blastermind.controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.bignerdranch.blastermind.android.blastermind.backend.DataManager;
import com.bignerdranch.blastermind.android.blastermind.event.FeedbackEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;
import com.bignerdranch.blastermind.android.blastermind.view.SubmitButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class GameFragment extends BaseFragment implements GameActivity.BackPressedCallback, BaseActivity.BrightnessCallbacks {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final String TAG_WINNER_DIALOG = "MainFragment.TAG_WINNER_DIALOG";
    private static final String TAG_LOSER_DIALOG = "MainFragment.TAG_LOSER_DIALOG";
    private static final String EXIT_MATCH_TAG = "GameFragment.EXIT_MATCH_TAG";

    private static final int REQUEST_EXIT_MATCH_DIALOG = 1;
    private static final int REQUEST_END_OF_GAME_DIALOG = 2;

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

    public static Fragment newInstance() {
        return new GameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.inject(this, view);

        createRows();

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mDataManager.getCurrentMatchName());
        }

        ((BaseActivity) getActivity()).registerBrightnessCallbacks(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupInputButtons(); // we want to setup input buttons after the rest of the view has been created
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((BaseActivity) getActivity()).unregisterBrightnessCallbacks(this);
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

        mSubmitButton.setEnabled(false);

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
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mGuessContainerHeightPx = mGuessContainer.getHeight();

                // now that we have the height of the container, only now can we create our first guess row
                mRowHeightPx = mGuessContainerHeightPx / Logic.guessLimit;
                populateEmptyRows();

                // cleanup by removing listener
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    mGuessContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    mGuessContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
        // get types in order
        for (int i = 0; i < TYPE.values().length; i++) {
            for (final TYPE type : TYPE.values()) {
                if (type.getPosition() == i) {
                    setupInputButton(type);
                }
            }
        }

        // add submit button
        setupSubmitButton();
        mInputContainer.addView(mSubmitButton);
    }

    private void setupInputButton(final TYPE type) {
        InputButton inputButton = new InputButton(getActivity());

        TypedArray typedArray = getResources().obtainTypedArray(R.array.guess_colors);
        int index = type.getPosition();
        int color = typedArray.getColor(index, 0);
        typedArray.recycle();
        inputButton.setColor(color);

        // set layout params
        int margin = (int) getResources().getDimension(R.dimen.input_button_margin);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(margin, margin, margin, margin);
        inputButton.setLayoutParams(layoutParams);

        // setup click listener
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentGuessRow.setActivePegType(type);
                setStateOfSubmitButton();
            }
        });

        mInputContainer.addView(inputButton);
    }

    private void setupSubmitButton() {
        mSubmitButton = new SubmitButton(getActivity());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSubmitButton.setLayoutParams(layoutParams);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guess guess = mCurrentGuessRow.getGuess();
                mDataManager.sendGuess(guess);
                mSubmitButton.setPending();
            }
        });
    }

    private void setStateOfSubmitButton() {
        if (mCurrentGuessRow == null) { // setting state for the first time
            mSubmitButton.setEnabled(false);
            return;
        }

        // if all pegs have a type then and only then enable update button
        mSubmitButton.setEnabled(mCurrentGuessRow.areAllPegsSet());
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

    @Override
    public void setBrightness(int brightness) {
        // set background color
        int backgroundColor;
        TypedArray typedArray = getResources().obtainTypedArray(R.array.background_brightness);
        backgroundColor = typedArray.getColor(brightness, 0);
        typedArray.recycle();

        mGuessContainer.setBackgroundColor(backgroundColor);
        mInputContainer.setBackgroundColor(backgroundColor);

        // set text color
        int maxBrightness = getResources().getInteger(R.integer.max_brightness_setting);
        int medianBrightness = (int) ((float) maxBrightness / 2);
        int textColor;
        if (brightness <= medianBrightness) {
            textColor= getResources().getColor(R.color.light_text);
        } else {
            textColor = getResources().getColor(R.color.dark_text);
        }
    }

    @Override
    public boolean registerForEvents() {
        return true;
    }
}