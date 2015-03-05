package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PegView extends RelativeLayout {

    @InjectView(R.id.view_peg_border)
    protected Button mBorder;
    @InjectView(R.id.view_peg_button)
    protected Button mButton;

    private Logic.TYPE mType;

    public PegView(Context context) {
        this(context, null);
    }

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Logic.TYPE getType() {
        return mType;
    }

    public void setType(Logic.TYPE type) {
        mType = type;
        setColor(mType.getRgb());
    }

    public void setActive() {
        Drawable drawable = getResources().getDrawable(R.drawable.active_button_border);
        mBorder.setBackground(drawable);
    }

    public void setInactive() {
        Drawable drawable = getResources().getDrawable(R.drawable.inactive_button_border);
        mBorder.setBackground(drawable);
    }

    public boolean isSet() {
        return mType != null;
    }

    public void reset() {
        setInactive();
        setColor(null);
        mType = null;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_peg, this);
        ButterKnife.inject(this, view);

        // setup button
        int buttonSize = (int) getResources().getDimension(R.dimen.peg_size);
        LayoutParams buttonParams = new LayoutParams(buttonSize, buttonSize);
        buttonParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mButton.setLayoutParams(buttonParams);

        // setup border
        int borderSize = (int) getResources().getDimension(R.dimen.peg_border_thickness);
        LayoutParams borderParams = new LayoutParams(borderSize, borderSize);
        borderParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBorder.setLayoutParams(borderParams);

        reset();
    }

    private void setColor(@Nullable String color) {
        if (color == null)  {
            mButton.setBackground(null);
            return;
        }

        int rgbColor = Color.parseColor(color);
        Drawable drawable = getResources().getDrawable(R.drawable.peg);
        drawable.setColorFilter(rgbColor, PorterDuff.Mode.SRC_ATOP);
        mButton.setBackground(drawable);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // intercept touch events for button and border
        return onTouchEvent(ev);
    }
}
