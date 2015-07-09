package com.tir38.android.blastermind.core;

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
