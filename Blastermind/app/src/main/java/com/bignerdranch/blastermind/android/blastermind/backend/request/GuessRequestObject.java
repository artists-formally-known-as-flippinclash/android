package com.bignerdranch.blastermind.android.blastermind.backend.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuessRequestObject {

    @SerializedName("sequence")
    List<String> mTypes;

    public void setTypes(List<String> types) {
        mTypes = types;
    }
}
