package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class InputButton extends Button {
    public InputButton(Context context) {
        super(context);
    }

    public InputButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }
}
