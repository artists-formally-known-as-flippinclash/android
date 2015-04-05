package com.bignerdranch.blastermind.andorid.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class MatchEnd {
    private int mMatchId;
    private int mWinnerId;
    private String mWinnerName;
    private Guess mSolution;
}
