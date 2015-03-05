package com.bignerdranch.blastermind.android.blastermind.backend;

import com.bignerdranch.blastermind.android.blastermind.backend.request.GuessBody;
import com.bignerdranch.blastermind.android.blastermind.backend.response.GuessResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BlasterRestService {

    @POST(API.GUESS)
    public void sendGuess(@Path("match_id") int matchId, @Body GuessBody body, Callback<GuessResponse> responseCallback);

    public static class API {
        public static final String MATCHES = "/matches";
        public static final String MATCH = MATCHES + "/{match_id}";
        public static final String GUESS = MATCH + "/guesses";
    }
}
