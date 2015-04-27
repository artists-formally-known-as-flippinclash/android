package com.tir38.android.blastermind.event;

import com.bignerdranch.blastermind.andorid.core.Feedback;

public class FeedbackEvent {

    private final Feedback mFeedback;

    public FeedbackEvent(Feedback feedback) {
        mFeedback = feedback;
    }

    public Feedback getFeedback() {
        return mFeedback;
    }
}
