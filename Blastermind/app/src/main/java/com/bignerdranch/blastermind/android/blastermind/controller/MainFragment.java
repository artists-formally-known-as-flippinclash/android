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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainFragment extends Fragment {

    private Guess mGuess;
    private int mSize;

    @InjectView(R.id.update_button)
    Button mUpdateButton;
    private GuessRowView mGuessRowView;

    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSize = 4;
        mGuess = new Guess(mSize);
        ArrayList<Logic.TYPE> types = new ArrayList<>(mSize);
        types.add(Logic.TYPE.Blue);
        types.add(Logic.TYPE.Red);
        types.add(Logic.TYPE.Yellow);
        types.add(Logic.TYPE.Green);
        mGuess.setTypes(types);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_main_root);

        mGuessRowView = new GuessRowView(getActivity());
        mGuessRowView.setup(mSize);
        mGuessRowView.setGuess(mGuess);
        linearLayout.addView(mGuessRowView);

        return view;
    }

    @OnClick(R.id.update_button)
  public void onUpdateClick() {
        // get guessRow an change guess
        Guess newGuess = new Guess(mSize);
        ArrayList<Logic.TYPE> types = new ArrayList<>(mSize);
        types.add(Logic.TYPE.Purple);
        types.add(Logic.TYPE.Red);
        types.add(Logic.TYPE.Yellow);
        types.add(Logic.TYPE.Green);
        newGuess.setTypes(types);
        mGuessRowView.setGuess(newGuess);
  }

}
