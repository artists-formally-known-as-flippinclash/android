package com.bignerdranch.blastermind.android.blastermind.event;

public class MatchCreateSuccessEvent {

    private final String mMatchName;

    public MatchCreateSuccessEvent(String matchName) {
        mMatchName = matchName;
    }

    public String getMatchName() {
        return mMatchName;
    }
}
