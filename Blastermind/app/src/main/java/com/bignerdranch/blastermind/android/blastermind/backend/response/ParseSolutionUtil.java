package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;

import java.util.ArrayList;
import java.util.List;

public class ParseSolutionUtil {


    private ParseSolutionUtil() {
        // can't instantiate util class
    }

    public static Guess parseGuessFromSolution(List<String> solutionStrings) {
        Logic.TYPE[] types = Logic.TYPE.values();

        List<Logic.TYPE> solutionTypes = new ArrayList<>();

        for (String solutionType: solutionStrings) {
            for (Logic.TYPE type: types) {
                if (type.name().equals(solutionType)) {
                    solutionTypes.add(type);
                }
            }
        }

        Guess solution = new Guess(solutionTypes);
        return solution;
    }
}
