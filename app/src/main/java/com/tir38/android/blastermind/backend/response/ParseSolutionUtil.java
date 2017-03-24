package com.tir38.android.blastermind.backend.response;

import com.tir38.android.blastermind.core.Guess;
import com.tir38.android.blastermind.core.Logic;

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
