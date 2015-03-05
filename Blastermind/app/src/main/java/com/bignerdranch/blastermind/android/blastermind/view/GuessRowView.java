package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Logic;

import java.util.ArrayList;

public class GuessRowView extends LinearLayout {

    private static final String TAG = GuessRowView.class.getSimpleName();
    private Context mContext;
    private ArrayList<PegView> mPegViews;
    private int mActivePegIndex; // index in mPegsVie for which peg is active; -1 == all pegs are set

    public GuessRowView(Context context) {
        this (context, null);
    }

    public GuessRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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

                    makeAllPegsInactive();

                    // and set this one to active
                    pegView.setActive();
                }
            });

            // add to view and array
            addView(pegView);
            mPegViews.add(pegView);

            // set first item to active
            mPegViews.get(mActivePegIndex).setActive();
        }
    }

    @Nullable
    public PegView getActivePeg() {
        if (mActivePegIndex == -1) {
            return null;
        }
        return mPegViews.get(mActivePegIndex);
    }

    public void setActivePegType(Logic.TYPE type) {
        if (getActivePeg() == null) {
            return;
        }
        getActivePeg().setColor(type.getRgb());
        advanceActivePeg();
    }

    /**
     * advance to the next activatable peg (i.e. the next peg that hasn't been set yet)
     */
    private void advanceActivePeg() {
        makeAllPegsInactive();

        for (int i = 0; i< mPegViews.size(); i++) {
            PegView pegView = mPegViews.get(i);
            if (!pegView.isSet()){
                pegView.setActive();
                mActivePegIndex = i;
                return;
            }
        }

        // all pegs are set
        mActivePegIndex = -1;
    }

    private void makeAllPegsInactive() {
        // clear active state on all pegs
        for (PegView tempPegView: mPegViews) {
            tempPegView.setInactive();
        }
    }
}
