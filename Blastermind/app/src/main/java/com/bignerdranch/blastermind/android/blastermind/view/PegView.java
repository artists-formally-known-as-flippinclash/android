package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

public class PegView extends Button {

    private static final String EMPTY_COLOR = "#111111"; // near black

    public PegView(Context context) {
        super(context);
    }

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }


    public void reset() {
        setColor(EMPTY_COLOR);
    }
}
