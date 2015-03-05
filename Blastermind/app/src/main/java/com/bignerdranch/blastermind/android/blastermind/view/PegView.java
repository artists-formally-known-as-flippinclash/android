package com.bignerdranch.blastermind.android.blastermind.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.android.blastermind.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PegView extends RelativeLayout {

    private static final String EMPTY_COLOR = "#111111"; // near black

    @InjectView(R.id.view_peg_background)
    protected TextView mBackground;
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
//        Drawable drawable = getResources().getDrawable(R.drawable.circle_button);
//        setBackground(drawable);
        mButton.setText("X");
    }

    public void setInactive() {
//        setBackground(null);
        mButton.setText("");
    }

    public boolean isSet() {
        return mType != null;
    }

    public void reset() {
        setColor(EMPTY_COLOR);
        mType = null;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_peg, this);
        ButterKnife.inject(this, view);
    }


    private void setColor(String color) {
        int rgbColor = Color.parseColor(color);
        setBackgroundColor(rgbColor);
    }

}
