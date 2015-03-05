package com.bignerdranch.blastermind.andorid.core;

public class Feedback {

    private int mMatchId;
    private String mOutcome;
    private int mPositionCount; // how many pegs are the right color AND in the right position
    private int mColorCount; // how many pegs are the right color;

    public int getMatchId() {
        return mMatchId;
    }

    public void setMatchId(int matchId) {
        mMatchId = matchId;
    }

    public String getOutcome() {
        return mOutcome;
    }

    public void setOutcome(String outcome) {
        mOutcome = outcome;
    }

    public int getPositionCount() {
        return mPositionCount;
    }

    public void setPositionCount(int positionCount) {
        mPositionCount = positionCount;
    }

    public int getColorCount() {
        return mColorCount;
    }

    public void setColorCount(int colorCount) {
        mColorCount = colorCount;
    }
}
