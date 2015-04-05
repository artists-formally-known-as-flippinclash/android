package com.bignerdranch.blastermind.andorid.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class Feedback {
    private int mMatchId;
    private String mOutcome;
    private int mPositionCount; // how many pegs are the right type AND in the right position
    private int mTypeCount; // how many pegs are the right type;
}
