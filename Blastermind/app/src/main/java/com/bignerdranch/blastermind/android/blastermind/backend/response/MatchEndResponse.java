package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.bignerdranch.blastermind.andorid.core.Guess;

import java.util.List;

public class MatchEndResponse {
    Data data;

    private class Data {
        int id;
        String channel;
        String state;
        String name;
        Winner winner;
        List<Round> rounds;

        private class Winner {
            int id;
            String name;
        }

        private class Round {
            int id;
            List<String> solution;
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
        if (data.winner == null) {
            return -1; // nobody won
        }
        return data.winner.id;
    }

    public String getWinnerName() {
        if (data.winner == null) {
            return null; // nobody won;
        }
        return data.winner.name;
    }

    // for now return solution of the last round only (assume a match has only one round)
    public Guess getSolution() {
        int numRounds = data.rounds.size();
        Data.Round lastRound = data.rounds.get(numRounds - 1);
        List<String> solutionStrings = lastRound.solution;
        return ParseSolutionUtil.parseGuessFromSolution(solutionStrings);
    }

    // TODO parse the rest of the response
}




