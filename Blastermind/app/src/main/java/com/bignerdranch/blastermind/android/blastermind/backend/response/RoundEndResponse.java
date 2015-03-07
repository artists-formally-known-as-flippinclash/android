package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.bignerdranch.blastermind.andorid.core.Guess;

import java.util.List;

/**
 * GSON object for round end
 */
public class RoundEndResponse {

    private static final String TAG = RoundEndResponse.class.getSimpleName();
    Data data;

    private class Data {
        int id;
        List<String> solution;
        Winner winner;

        private class Winner {
            int id;
            String name;
        }
    }

   public int getMatchId() {
       return data.id;
   }

   public Guess getSolution() {
       List<String> solutionStrings = data.solution;
       return ParseSolutionUtil.parseGuessFromSolution(solutionStrings);
   }

    public int getWinnerId() {
        if (data.winner == null)  {
            return -1; // nobody won
        }
        return data.winner.id;
    }

    public String getWinnerName() {
        if (data.winner == null) {
            return null; // nobody won
        }
        return data.winner.name;
    }
}
