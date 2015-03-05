package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class PegView extends Button {

    private static final String EMPTY_COLOR = "#111111"; // near black

    public PegView(Context context) {
        super(context);
    }

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LinearLayout.LayoutParams(20, 20)); // set size
    }

    public void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }


    public void reset() {
        setColor(EMPTY_COLOR);
    }
}
