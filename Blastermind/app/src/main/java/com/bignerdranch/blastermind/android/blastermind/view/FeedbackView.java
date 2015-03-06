package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.bignerdranch.blastermind.android.blastermind.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackView extends LinearLayout {

    private static final String TAG = FeedbackView.class.getSimpleName();
    @InjectView(R.id.feedback_peg1)
    ImageView mFeedbackPeg1;
    @InjectView(R.id.feedback_peg2)
    ImageView mFeedbackPeg2;
    @InjectView(R.id.feedback_peg3)
    ImageView mFeedbackPeg3;
    @InjectView(R.id.feedback_peg4)
    ImageView mFeedbackPeg4;
    private List<ImageView> mFeedbackPegs;

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
        mFeedbackPegs = new ArrayList<>();
        mFeedbackPegs.add(mFeedbackPeg1);
        mFeedbackPegs.add(mFeedbackPeg2);
        mFeedbackPegs.add(mFeedbackPeg3);
        mFeedbackPegs.add(mFeedbackPeg4);
    }

    public void update(Feedback feedback) {
        int index = 0;
        int numPositionColor = feedback.getPositionCount();
        int numColor = feedback.getColorCount();

        // set position and color pegs
        for (int i = index; i < numPositionColor; i++, index++) {
            markPegPositionColor(index);
        }

        // then set color pegs
        for (int i = index; i< numColor+numPositionColor; i++, index++)  {
            markPegColor(index);
        }
    }

    private void markPegPositionColor(int i) {
        int color = getResources().getColor(R.color.feedback_position_and_color_color);
        mFeedbackPegs.get(i).setBackgroundColor(color);

    }

    private void markPegColor(int i) {
        int color = getResources().getColor(R.color.feedback_color_color);
        mFeedbackPegs.get(i).setBackgroundColor(color);
    }
}
