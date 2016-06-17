package com.tir38.android.blastermind.backend;

import com.tir38.android.blastermind.backend.request.GuessBody;
import com.tir38.android.blastermind.backend.request.PlayerBody;
import com.tir38.android.blastermind.backend.response.GuessResponse;
import com.tir38.android.blastermind.backend.response.NullResponse;
import com.tir38.android.blastermind.backend.response.StartMatchResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface BlastermindWebService {

    String MATCHES = "/matches";
    String MATCH = MATCHES + "/{match_id}";
    String TRIGGER_MATCH_START = MATCH + "/start";
    String GUESS = MATCH + "/players/{player_id}/guesses";

    // TODO add headers to all requests
    @Headers("Content-Type: application/json")
    @POST(GUESS)
    Observable<GuessResponse> sendGuess(@Path("match_id") int matchId,
                                        @Path("player_id") int playerId,
                                        @Body GuessBody body);

    @Headers("Content-Type: application/json")
    @POST(MATCHES)
    Observable<StartMatchResponse> startMatch(@Body PlayerBody body);


    @Headers("Content-Type: application/json")
    @POST(TRIGGER_MATCH_START)
    Observable<NullResponse> triggerMatchStart(@Path("match_id") int matchId);
}
