package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.backend.LiveDataManager;
import com.bignerdranch.blastermind.android.blastermind.event.MatchEndedEvent;
import com.bignerdranch.blastermind.android.blastermind.event.MatchStartedEvent;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    @InjectView(R.id.update_button)
    protected Button mUpdateButton;
    @InjectView(R.id.fragment_main_guesses_container)
    protected LinearLayout mGuessContainer;
    @InjectView(R.id.fragment_main_input_container)
    protected LinearLayout mInputContainer;

    private int mCurrentTurn;
    private GuessRowView mCurrentGuessRow;


    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        LiveDataManager dataManager = new LiveDataManager();
        dataManager.setupConnection();
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

    @OnClick(R.id.update_button)
    public void onUpdateClick() {
        createEmptyGuessForNextTurn();
        setStateOfUpdateButton();
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
        int weight = 100 / Logic.guessWidth;

        // add input button to container
        InputButton inputButton = new InputButton(getActivity());
        inputButton.setColor(type.getRgb());
        inputButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight));
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
}
