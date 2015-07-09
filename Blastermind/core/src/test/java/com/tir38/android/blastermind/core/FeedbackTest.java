package com.tir38.android.blastermind.core;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedbackTest {

    private Feedback mFeedback;

    @Before
    public void setUp() throws Exception {
        mFeedback = new Feedback();
    }

    @Test
    public void feedbackHasMatchId() throws Exception {
        mFeedback.setMatchId(123);
        assertThat(mFeedback.getMatchId()).isEqualTo(123);
    }

    @Test
    public void feedbackHadOutcome() throws Exception {
        mFeedback.setOutcome("incorrect");
        assertThat(mFeedback.getOutcome()).isEqualTo("incorrect");
    }

    @Test
    public void feedbackHasPositionCount() throws Exception {
        mFeedback.setPositionCount(3);
        assertThat(mFeedback.getPositionCount()).isEqualTo(3);
    }

    @Test
    public void feedbackHasTypeCount() throws Exception {
        mFeedback.setTypeCount(2);
        assertThat(mFeedback.getTypeCount()).isEqualTo(2);
    }
}
