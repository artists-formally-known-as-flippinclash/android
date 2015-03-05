package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class PegView extends Button {

    private static final String EMPTY_COLOR = "#111111"; // near black

    private String mColor;

    public PegView(Context context) {
        super(context);
    }

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(String color) {
        mColor = color;
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }

    public void setActive() {
        setText("X");
    }

    public void setInactive() {
        setText("");
    }

    public boolean isSet() {
        return mColor != null;
    }

    public void reset() {
        setColor(EMPTY_COLOR);
        mColor = null;
    }
}
