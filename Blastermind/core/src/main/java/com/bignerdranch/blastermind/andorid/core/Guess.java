package com.bignerdranch.blastermind.andorid.core;

import java.util.List;

public class Guess {

    private List<Logic.TYPE> mTypes;

    public Guess(List<Logic.TYPE> types) {
        if (types.size() != Logic.guessWidth) {
            throw new IllegalArgumentException("types must be of size: " + Logic.guessWidth);
        }
        this.mTypes = types;
    }

    public List<Logic.TYPE> getTypes() {
        return mTypes;
    }
}
