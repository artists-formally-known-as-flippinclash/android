package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackView extends LinearLayout {

    @InjectView(R.id.feedback_peg1)
    ImageView mFeedbackPeg1;
    @InjectView(R.id.feedback_peg2)
    ImageView mFeedbackPeg2;
    @InjectView(R.id.feedback_peg3)
    ImageView mFeedbackPeg3;
    @InjectView(R.id.feedback_peg4)
    ImageView mFeedbackPeg4;

    public FeedbackView(Context context) {
        this(context, null);
    }

    public FeedbackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_feedback, this);
        ButterKnife.inject(this, view);
    }

}
