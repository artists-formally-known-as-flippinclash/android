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
        List<ExistingPlayer> players; // the last player in this list is ME. get my ID from there

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

    public int getMyId() {
        // ugly hack: assume that the last player in the list (the most recent) is me
        Data.ExistingPlayer me = data.players.get(data.players.size() - 1);
        return me.id;
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
