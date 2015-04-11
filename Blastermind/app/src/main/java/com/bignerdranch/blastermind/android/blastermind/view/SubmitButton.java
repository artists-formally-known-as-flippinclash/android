package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Button has three states:
 * - enabled
 * - disabled
 * - pending
 */
public class SubmitButton extends FrameLayout {

    @InjectView(R.id.button_submit_progress)
    protected ProgressBar mProgressBar;
    @InjectView(R.id.button_submit_disable)
    protected ImageView mDisabledImage;
    @InjectView(R.id.button_submit_enable)
    protected ImageView mEnabledImage;

    public SubmitButton(Context context) {
        this(context, null);
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.button_submit, this);
        ButterKnife.inject(this, view);

        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mProgressBar.setVisibility(GONE);
        if (enabled) {
            mDisabledImage.setVisibility(GONE);
            mEnabledImage.setVisibility(VISIBLE);
        } else {
            mDisabledImage.setVisibility(VISIBLE);
            mEnabledImage.setVisibility(GONE);
        }
    }

    public void setPending() {
        mProgressBar.setVisibility(VISIBLE);
        mDisabledImage.setVisibility(GONE);
        mEnabledImage.setVisibility(GONE);
    }
}
