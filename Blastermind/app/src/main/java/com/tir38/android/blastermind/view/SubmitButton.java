package com.tir38.android.blastermind.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tir38.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SubmitButton extends FrameLayout {

    private static final String TAG = SubmitButton.class.getSimpleName();

    @InjectView(R.id.progress)
    protected ProgressBar mProgressIndicator;
    @InjectView(R.id.send_enable)
    protected ImageView mSendEnabledIcon;
    @InjectView(R.id.send_disabled)
    protected ImageView mSendDisabledIcon;

    public SubmitButton(Context context) {
        this(context, null);
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.button_submit, this);
        ButterKnife.inject(this, view);

        setState(STATE.DISABLED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException("This View does not support direct usage of setEnabled(). Instead use setState().");
    }

    /**
     * Use this to set button enabled, disabled, or "pending".
     *
     * @param state
     */
    public void setState(STATE state) {
        // use VISIBLE / INVISIBLE instead of VISIBLE/GONE so that view doesn't resize itself
        // call to super.setEnabled() is just used to handle all other aspects of setEnabled() except for visual state change (i.e. disableing click)


        switch (state) {
            case PENDING:
                mProgressIndicator.setVisibility(VISIBLE);
                mSendEnabledIcon.setVisibility(INVISIBLE);
                mSendDisabledIcon.setVisibility(INVISIBLE);
                super.setEnabled(false);
                break;

            case ENABLED:
                mProgressIndicator.setVisibility(INVISIBLE);
                mSendEnabledIcon.setVisibility(VISIBLE);
                mSendDisabledIcon.setVisibility(INVISIBLE);
                super.setEnabled(true);
                break;

            case DISABLED:
                mProgressIndicator.setVisibility(INVISIBLE);
                mSendEnabledIcon.setVisibility(INVISIBLE);
                mSendDisabledIcon.setVisibility(VISIBLE);
                super.setEnabled(false);
                break;
        }
    }

    /**
     * Button has three states:
     * - enabled
     * - disabled
     * - pending
     */
    public enum STATE {
        PENDING,
        ENABLED,
        DISABLED
    }
}
