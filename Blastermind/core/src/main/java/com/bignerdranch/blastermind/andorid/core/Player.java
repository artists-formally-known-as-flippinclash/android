package com.bignerdranch.blastermind.andorid.core;

import java.io.Serializable;

public class Player implements Serializable {

    private final String mName;

    public Player(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
