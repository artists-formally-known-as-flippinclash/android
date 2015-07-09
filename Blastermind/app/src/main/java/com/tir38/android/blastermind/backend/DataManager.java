package com.tir38.android.blastermind.backend;

import com.tir38.android.blastermind.core.Guess;
import com.tir38.android.blastermind.core.Player;

import java.util.List;

public interface DataManager {

    void startMatch(Player player);

    void sendGuess(Guess guess);

    String getCurrentMatchName();

    Player getCurrentPlayer();

    boolean isCurrentMatchMultiplayer();

    List<String> getCurrentMatcpPlayers();

}
