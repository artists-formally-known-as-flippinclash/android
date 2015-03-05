package com.bignerdranch.blastermind.android.blastermind.backend;

import com.bignerdranch.blastermind.andorid.core.Guess;

public interface DataManager {

    public void setupConnection();

    public void sendGuess(Guess guess);
}
