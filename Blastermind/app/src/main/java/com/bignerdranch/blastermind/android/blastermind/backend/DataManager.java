package com.bignerdranch.blastermind.android.blastermind.backend;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Player;

public interface DataManager {

    public void startMatch(Player player);

    public void sendGuess(Guess guess);
}
