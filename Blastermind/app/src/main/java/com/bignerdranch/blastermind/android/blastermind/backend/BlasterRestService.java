package com.bignerdranch.blastermind.android.blastermind.backend;

import com.bignerdranch.blastermind.android.blastermind.backend.request.GuessBody;
import com.bignerdranch.blastermind.android.blastermind.backend.request.PlayerBody;
import com.bignerdranch.blastermind.android.blastermind.backend.response.GuessResponse;
import com.bignerdranch.blastermind.android.blastermind.backend.response.StartMatchResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface BlasterRestService {

    @Headers("Content-Type: application/json")
    @POST(API.GUESS)
    public void sendGuess(@Path("match_id") int matchId, @Path("player_id") int playerId, @Body GuessBody body, Callback<GuessResponse> responseCallback);

    @Headers("Content-Type: application/json")
    @POST(API.MATCHES)
    public void startMatch(@Body PlayerBody body, Callback<StartMatchResponse> responseCallback);

    public static class API {
        public static final String MATCHES = "/matches";
        public static final String MATCH = MATCHES + "/{match_id}";
        public static final String GUESS = MATCH + "/players/{player_id}/guesses";
    }
}
