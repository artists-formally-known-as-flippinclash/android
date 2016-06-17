package com.tir38.android.blastermind.event;

import com.tir38.android.blastermind.core.Feedback;

public class FeedbackEvent {

    private final Feedback mFeedback;

    public FeedbackEvent(Feedback feedback) {
        mFeedback = feedback;
    }

    public Feedback getFeedback() {
        return mFeedback;
    }
}
