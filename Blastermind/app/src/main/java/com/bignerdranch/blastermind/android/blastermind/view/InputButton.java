package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.bignerdranch.blastermind.android.blastermind.R;


public class InputButton extends ImageButton {

    public InputButton(Context context) {
        this(context, null);
    }

    public InputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int padding = (int) getResources().getDimension(R.dimen.input_button_padding);
        setPadding(padding, padding, padding, padding);
    }

    public void setColor(@ColorRes int color) {
        Drawable drawable = getResources().getDrawable(R.drawable.input_button);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        setImageDrawable(drawable);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.min(width, height);

        // make square but never larger than max allowed size
        int maxAllowedSize = (int) getResources().getDimension(R.dimen.max_input_button_size);
        size = Math.min(size, maxAllowedSize);

        setMeasuredDimension(size, size);
    }

}
