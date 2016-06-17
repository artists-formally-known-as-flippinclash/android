package com.tir38.android.blastermind.core;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class Player implements Serializable {

    private final String mName; // unique identifier on client
    private int mId; // unique identifier on server

    public Player(String name) {
        mName = name;
    }
}
