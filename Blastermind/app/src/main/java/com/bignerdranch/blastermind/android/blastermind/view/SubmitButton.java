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

public class SubmitButton extends FrameLayout {

    private static final String TAG = SubmitButton.class.getSimpleName();
    @InjectView(R.id.button_submit_progress)
    protected ProgressBar mProgressIndicator;
    @InjectView(R.id.send_icon)
    protected ImageView mSendIcon;

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

        switch (state) {
            case PENDING:
                mProgressIndicator.setVisibility(VISIBLE);
                mSendIcon.setVisibility(INVISIBLE);
                super.setEnabled(false);
                break;

            case ENABLED:
                mProgressIndicator.setVisibility(INVISIBLE);
                mSendIcon.setVisibility(VISIBLE);
                super.setEnabled(true);
                break;

            case DISABLED:
                mProgressIndicator.setVisibility(INVISIBLE);
                mSendIcon.setVisibility(VISIBLE);
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
