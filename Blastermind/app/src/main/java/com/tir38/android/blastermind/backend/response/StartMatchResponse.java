package com.tir38.android.blastermind.backend.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StartMatchResponse {

    @SerializedName("data")
    Data data;
    @SerializedName("you")
    You you;

    private class Data {
        @SerializedName("id")
        int matchId;
        @SerializedName("channel")
        String channel;
        @SerializedName("state")
        String state;
        @SerializedName("name")
        String matchName;
        @SerializedName("players")
        List<ExistingPlayer> players; // one of these players will be "you"

        private class ExistingPlayer {
            @SerializedName("id")
            int id;
            @SerializedName("name")
            String name;
        }
    }

    private class You {
        @SerializedName("id")
        int id;
        @SerializedName("name")
        String name;
    }

    public String getChannel() {
        return data.channel;
    }

    public String getMatchName() {
        return data.matchName;
    }

    public int getMyId() {
        return you.id;
    }

    public List<String> getExistingPlayers() {
        List<String> existingPlayers = new ArrayList<>();

        for (Data.ExistingPlayer existingPlayer : data.players) {
            existingPlayers.add(existingPlayer.name);
        }
        return existingPlayers;
    }

    public int getMatchId() {
        return data.matchId;
    }
}
