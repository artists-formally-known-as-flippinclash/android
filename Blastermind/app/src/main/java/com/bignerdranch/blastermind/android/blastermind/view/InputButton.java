package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.Button;

import com.bignerdranch.blastermind.android.blastermind.R;

public class InputButton extends Button {

    // default minimum is 48 dp; per Material design specs
    private static final int MINIMUM_HEIGHT_DP = 36;

    public InputButton(Context context) {
        this(context, null);
    }

    public InputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
    }

    public void setColor(@ColorRes int color) {
        Drawable drawable = getResources().getDrawable(R.drawable.input_button);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        setBackground(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int minHeightPx = (int) (getResources().getDisplayMetrics().density * MINIMUM_HEIGHT_DP);
        size = Math.max(size, minHeightPx);
        setMeasuredDimension(size, size);
    }
}
