package com.tir38.android.blastermind.backend;

import com.tir38.android.blastermind.core.Guess;
import com.tir38.android.blastermind.core.Player;

import java.util.List;

/**
 * The central brain. Handles interaction with servers and local storage.
 */
public interface GameSupervisor {

    void startMatch(Player player);

    void sendGuess(Guess guess);

    String getCurrentMatchName();

    Player getCurrentPlayer();

    boolean isCurrentMatchMultiplayer();

}
