package com.tir38.android.blastermind.backend;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Player;

import java.util.List;

public interface DataManager {

    void startMatch(Player player);

    void sendGuess(Guess guess);

    String getCurrentMatchName();

    Player getCurrentPlayer();

    boolean isCurrentMatchMultiplayer();

    List<String> getCurrentMatcpPlayers();

}
