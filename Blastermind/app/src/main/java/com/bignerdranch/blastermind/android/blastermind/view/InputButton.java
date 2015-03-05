package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.bignerdranch.blastermind.android.blastermind.R;

public class InputButton extends Button {
    public InputButton(Context context) {
        super(context);
    }

    public InputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        Drawable drawable = getResources().getDrawable(R.drawable.input_button);
        drawable.setColorFilter(rgbColor, PorterDuff.Mode.SRC_ATOP);
        setBackground(drawable);
    }
}
