package com.tir38.android.blastermind.event;

import com.tir38.android.blastermind.core.MatchEnd;

public class MatchEndedEvent {

    private final MatchEnd mMatchEnd;

    public MatchEndedEvent(MatchEnd matchEnd) {
        mMatchEnd = matchEnd;
    }

    public MatchEnd getMatchEnd() {
        return mMatchEnd;
    }
}
