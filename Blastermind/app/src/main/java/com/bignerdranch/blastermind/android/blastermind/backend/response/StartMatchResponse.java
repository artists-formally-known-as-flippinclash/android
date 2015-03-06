package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StartMatchResponse {

    @SerializedName("data")
    Data data;

    private class Data {
        @SerializedName("id")
        int matchId;
        @SerializedName("channel")
        String channel;
        @SerializedName("state")
        String state;
        @SerializedName("players")
        List<ExistingPlayer> players;

        private class ExistingPlayer {
            @SerializedName("id")
            int id;
            @SerializedName("name")
            String name;
        }
    }

    public String getChannel() {
        return data.channel;
    }

    public List<String> getExistingPlayers() {
        List<String> existingPlayers = new ArrayList<>();

        for (Data.ExistingPlayer existingPlayer : data.players) {
            existingPlayers.add(existingPlayer.name);
        }
        return existingPlayers;
    }
}
