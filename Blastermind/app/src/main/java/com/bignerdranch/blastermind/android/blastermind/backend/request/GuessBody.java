package com.bignerdranch.blastermind.android.blastermind.backend.request;


import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  GSON to POST Guess POJO to webservice
 */
public class GuessBody {

    @SerializedName("guess")
    GuessRequest mGuess;

    public static GuessBody mapGuessToBody(Guess guess) {

        GuessBody guessBody = new GuessBody();

        List<Logic.TYPE> types = guess.getTypes();

        // map types to string
        List<String> sequence = new ArrayList<>();
        for (Logic.TYPE type: types) {
            sequence.add(type.name());
        }

        guessBody.mGuess.setTypes(sequence);
        return guessBody;
    }

    public GuessBody() {
        mGuess = new GuessRequest();
    }

    private class GuessRequest {

        @SerializedName("sequence")
        List<String> mTypes;

        public void setTypes(List<String> types) {
            mTypes = types;
        }
    }
}
