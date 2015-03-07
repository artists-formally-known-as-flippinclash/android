package com.bignerdranch.blastermind.android.blastermind.backend.response;

public class MatchEndResponse {
    Data data;

    private class Data {
        int id;
        String channel;
        String state;
        String name;
        Winner winner;

        private class Winner {
            int id;
            String name;
        }
    }

    public int getMatchId() {
        return data.id;
    }

    public String getChannel() {
        return data.channel;
    }

    public String getMatchName() {
        return data.name;
    }

    public int getWinnerId() {
        return data.winner.id;
    }

    public String getWinnerName() {
        return data.winner.name;
    }

    // TODO parse the rest of the response
}




