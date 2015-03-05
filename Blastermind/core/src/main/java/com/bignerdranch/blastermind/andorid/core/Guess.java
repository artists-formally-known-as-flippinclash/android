package com.bignerdranch.blastermind.andorid.core;

import java.util.ArrayList;

public class Guess {

    private final int mSize;
    private ArrayList<Logic.TYPE> mTypes;

    public Guess(int size) {
        mSize = size;
    }

    public ArrayList<Logic.TYPE> getTypes() {
        return mTypes;
    }

    public void setTypes(ArrayList<Logic.TYPE> types) {
        if (types.size() != mSize) {
            throw new IllegalArgumentException("types must be of size: " + mSize);
        }
        this.mTypes = types;
    }
}
