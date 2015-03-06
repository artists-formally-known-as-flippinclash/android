package com.bignerdranch.blastermind.andorid.core;

import java.io.Serializable;

public class Player implements Serializable {

    private final String mName; // unique identifier on client
    private int mId; // unique identifier on server

    public Player(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
