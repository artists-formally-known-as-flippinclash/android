package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;

import java.util.ArrayList;

public class GuessRowView extends LinearLayout {

    private Guess mGuess;
    private Context mContext;
    private ArrayList<PegView> mPegViews;

    public GuessRowView(Context context) {
        this (context, null);
    }

    public GuessRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setGuess(Guess guess) {
        mGuess = guess;
        // iterate through peg views and set color
        ArrayList<Logic.TYPE> types = mGuess.getTypes();
        for (int i = 0; i<types.size(); i++) {
            Logic.TYPE type = types.get(i);
            mPegViews.get(i).setColor(type.getRgb());
        }
    }

    public void setup(int numPegs) {
        mPegViews = new ArrayList<>(numPegs);
        for (int i = 0; i< numPegs; i++) {
            PegView pegView = new PegView(mContext);
            // add to view and array
            addView(pegView);
            mPegViews.add(pegView);
        }
    }
}
