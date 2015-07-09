package com.tir38.android.blastermind.backend.response;

import com.tir38.android.blastermind.core.Feedback;
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
            @SerializedName("peg_count")
            int typeCount;
            @SerializedName("position_count")
            int positionCount;

            @Override
            public String toString() {
                return "FeedbackResponse{" +
                        "typeCount=" + typeCount +
                        ", positionCount=" + positionCount +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "matchId=" + matchId +
                    ", outcome='" + outcome + '\'' +
                    ", feedback=" + feedback +
                    '}';
        }
    }

    public static Feedback mapResponseToFeedback(GuessResponse response) {
        Feedback feedback = new Feedback();
        feedback.setTypeCount(response.data.feedback.typeCount);
        feedback.setPositionCount(response.data.feedback.positionCount);
        feedback.setMatchId(response.data.matchId);
        feedback.setOutcome(response.data.outcome);

        return feedback;
    }


    @Override
    public String toString() {
        return "GuessResponse{" +
                "data=" + data +
                '}';
    }
}
