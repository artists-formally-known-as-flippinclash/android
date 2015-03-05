package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;

import java.util.ArrayList;

public class GuessRowView extends LinearLayout {

    private static final String TAG = GuessRowView.class.getSimpleName();
    private Guess mGuess;
    private Context mContext;
    private ArrayList<PegView> mPegViews;
    private int mActivePegIndex;

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

            final int index = i;

            final PegView pegView = new PegView(mContext);

            float weight = 100/ Logic.guessWidth;
            pegView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight));

            pegView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "clicked on item: " + index);
                    mActivePegIndex = index;
                    // clear active state on all pegs
                    for (PegView tempPegView: mPegViews) {
                        tempPegView.setInactive();
                    }
                    // and set this one to active
                    pegView.setActive();
                }
            });

            // add to view and array
            addView(pegView);
            mPegViews.add(pegView);
        }
    }

    public PegView getActivePeg() {
        return mPegViews.get(mActivePegIndex);
    }
}
