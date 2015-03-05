package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.bignerdranch.blastermind.andorid.core.Logic;

public class PegView extends Button {

    private static final String EMPTY_COLOR = "#111111"; // near black

    private Logic.TYPE mType;

    public PegView(Context context) {
        super(context);
    }

    public PegView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Logic.TYPE getType() {
        return mType;
    }

    public void setType(Logic.TYPE type) {
        mType = type;
        setColor(mType.getRgb());
    }

    public void setActive() {
        setText("X");
    }

    public void setInactive() {
        setText("");
    }

    public boolean isSet() {
        return mType != null;
    }

    public void reset() {
        setColor(EMPTY_COLOR);
        mType = null;
    }

    private void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }

}
