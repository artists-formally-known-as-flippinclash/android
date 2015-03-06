package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.bignerdranch.blastermind.andorid.core.Feedback;
import com.google.gson.annotations.SerializedName;

/**
 *  GSON to map webservice response to Feedback POJO
 */
public class GuessResponse {
    @SerializedName("data")
    Data data;

    private class Data {
        @SerializedName("id")
        int matchId;
        @SerializedName("outcome")
        String outcome;
        @SerializedName("feedback")
        FeedbackResponse feedback;

        private class FeedbackResponse {
            @SerializedName("color-count")
            int colorCount;
            @SerializedName("position-count")
            int positionCount;
        }
    }

    public static Feedback mapResponseToFeedback(GuessResponse response) {
        Feedback feedback = new Feedback();
        feedback.setColorCount(response.data.feedback.colorCount);
        feedback.setPositionCount(response.data.feedback.positionCount);
        feedback.setMatchId(response.data.matchId);
        feedback.setOutcome(response.data.outcome);

        return feedback;
    }
}
