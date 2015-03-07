package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;

import java.util.ArrayList;
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
       List<String> solutionString = data.solution;
       Logic.TYPE[] types = Logic.TYPE.values();

       List<Logic.TYPE> solutionTypes = new ArrayList<>();

       for (String solutionType: solutionString) {
           for (Logic.TYPE type: types) {
               if (type.name().equals(solutionType)) {
                   solutionTypes.add(type);
               }
           }
       }

       Guess solution = new Guess(Logic.guessWidth);
       solution.setTypes(solutionTypes);
       return solution;
   }

    public int getWinnerId() {
        return data.winner.id;
    }

    public String getWinnerName() {
        return data.winner.name;
    }
}
