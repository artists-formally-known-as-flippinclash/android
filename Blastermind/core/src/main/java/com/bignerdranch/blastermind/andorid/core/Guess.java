package com.bignerdranch.blastermind.andorid.core;

import java.util.ArrayList;

public class Guess {

    private final int mSize;
    private ArrayList<Integer> mValues;

    public Guess(int size) {
        mSize = size;
    }

    public ArrayList<Integer> getValues() {
        return mValues;
    }

    public void setValues(ArrayList<Integer> values) {
        if (values.size() != mSize) {
            throw new IllegalArgumentException("values must be of size: " + mSize);
        }
        this.mValues = values;
    }
}
