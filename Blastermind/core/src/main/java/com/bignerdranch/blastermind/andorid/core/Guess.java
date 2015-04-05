package com.bignerdranch.blastermind.andorid.core;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class Guess {

    private final List<Logic.TYPE> mTypes;

    public Guess(List<Logic.TYPE> types) {
        if (types.size() != Logic.guessWidth) {
            throw new IllegalArgumentException("types must be of size: " + Logic.guessWidth);
        }
        this.mTypes = types;
    }
}
