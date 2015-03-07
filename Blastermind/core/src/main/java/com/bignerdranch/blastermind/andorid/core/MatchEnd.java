package com.bignerdranch.blastermind.andorid.core;

public class MatchEnd {

    private int mMatchId;
    private int mWinnerId;
    private String mWinnerName;
    private Guess solution;

    public int getMatchId() {
        return mMatchId;
    }

    public void setMatchId(int matchId) {
        mMatchId = matchId;
    }

    public int getWinnerId() {
        return mWinnerId;
    }

    public void setWinnerId(int winnerId) {
        mWinnerId = winnerId;
    }

    public String getWinnerName() {
        return mWinnerName;
    }

    public void setWinnerName(String winnerName) {
        mWinnerName = winnerName;
    }

    public Guess getSolution() {
        return solution;
    }

    public void setSolution(Guess solution) {
        this.solution = solution;
    }
}
