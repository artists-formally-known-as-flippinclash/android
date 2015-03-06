package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.google.gson.annotations.SerializedName;

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
        List<PlayerBody> players;

        private class PlayerBody {
            @SerializedName("id")
            int id;
            @SerializedName("name")
            String name;
        }
    }
}
