package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;
import com.bignerdranch.blastermind.android.blastermind.view.InputButton;
import com.bignerdranch.blastermind.android.blastermind.view.PegView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.bignerdranch.blastermind.andorid.core.Logic.TYPE;

public class MainFragment extends Fragment {

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

        int weight = 100/ Logic.guessWidth;
        // get types in order
        for (int i = 0; i < TYPE.values().length; i++) {
            for (final TYPE type: TYPE.values()) {
                if (type.getPosition() == i) {
                    // add input button to container
                    InputButton inputButton = new InputButton(getActivity());
                    inputButton.setColor(type.getRgb());
                    inputButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight));
                    inputButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateActiveCell(type);
                        }
                    });
                    mInputContainer.addView(inputButton);
                }
            }
        }

        return view;
    }

    private void updateActiveCell(TYPE type) {
        PegView activePeg = mCurrentGuessRow.getActivePeg();
        activePeg.setColor(type.getRgb());
        mCurrentGuessRow.advanceActivePeg();
    }


    @OnClick(R.id.update_button)
    public void onUpdateClick() {

        if (mCurrentTurn > Logic.guessLimit) {
            mUpdateButton.setEnabled(false);
        }
        mCurrentTurn++;

        // create new row and add it
        mCurrentGuessRow = new GuessRowView(getActivity());
        mCurrentGuessRow.setup(Logic.guessWidth);
        mGuessContainer.addView(mCurrentGuessRow);
    }
}
