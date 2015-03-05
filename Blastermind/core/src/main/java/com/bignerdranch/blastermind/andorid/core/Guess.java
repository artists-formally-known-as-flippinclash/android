package com.bignerdranch.blastermind.andorid.core;

import java.util.List;

public class Guess {

    private final int mSize;
    private List<Logic.TYPE> mTypes;

    public Guess(int size) {
        mSize = size;
    }

    public List<Logic.TYPE> getTypes() {
        return mTypes;
    }

    public void setTypes(List<Logic.TYPE> types) {
        if (types.size() != mSize) {
            throw new IllegalArgumentException("types must be of size: " + mSize);
        }
        this.mTypes = types;
    }
}
