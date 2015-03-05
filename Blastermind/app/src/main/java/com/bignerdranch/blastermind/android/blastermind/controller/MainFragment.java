package com.bignerdranch.blastermind.android.blastermind.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;
import com.bignerdranch.blastermind.android.blastermind.view.GuessRowView;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private Guess mGuess;
    private int mSize;

    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSize = 4;
        mGuess = new Guess(mSize);
        ArrayList<Logic.TYPE> colors = new ArrayList<>(mSize);
        colors.add(Logic.TYPE.Blue);
        colors.add(Logic.TYPE.Red);
        colors.add(Logic.TYPE.Yellow);
        colors.add(Logic.TYPE.Green);
        mGuess.setTypes(colors);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_main_root);

        Button updateButton = (Button) view.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        GuessRowView guessRowView = new GuessRowView(getActivity());
        guessRowView.setup(mSize);
        guessRowView.setGuess(mGuess);
        linearLayout.addView(guessRowView);

        return view;
    }
}
