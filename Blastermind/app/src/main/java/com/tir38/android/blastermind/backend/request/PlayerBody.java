package com.tir38.android.blastermind.backend.request;

import com.tir38.android.blastermind.core.Player;
import com.google.gson.annotations.SerializedName;

/**
 *  GSON to POST Player POJO to webservice
 */
public class PlayerBody {

    @SerializedName("player")
    PlayerRequest playerRequest;

    public static PlayerBody mapPlayerToBody(Player player) {
        PlayerBody playerBody = new PlayerBody();
        playerBody.playerRequest.name = player.getName();
        return playerBody;
    }

    public PlayerBody() {
        playerRequest = new PlayerRequest();
    }

    private class PlayerRequest {
        @SerializedName("name")
        String name;
    }
}
