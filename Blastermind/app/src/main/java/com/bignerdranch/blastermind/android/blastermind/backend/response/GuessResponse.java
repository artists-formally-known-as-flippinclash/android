package com.bignerdranch.blastermind.android.blastermind.backend.response;

import com.google.gson.annotations.SerializedName;

public class GuessResponse {
    @SerializedName("data")
    Data data;

    private class Data {
        @SerializedName("type")
        String dataTtype;
        @SerializedName("id")
        int id;
        @SerializedName("outcome")
        String outcome;
        @SerializedName("feedback")
        Feedback feedback;

        private class Feedback {
            @SerializedName("color-count")
            int colorCount;
            @SerializedName("position-count")
            int positionCount;
        }
    }
}
