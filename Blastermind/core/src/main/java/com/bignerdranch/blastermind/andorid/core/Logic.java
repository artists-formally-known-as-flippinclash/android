package com.bignerdranch.blastermind.andorid.core;

public class Logic {

    // number of types in a guess
    public static final int guessWidth = 4;
    public static final int guessLimit = 10;

    public enum TYPE {
        alpha(0, "#F2220F"),
        beta(1, "#F29F05"),
        gamma(2, "#F2CB05"),
        delta(3, "#027333"),
        epsilon(4, "#034C8C"),
        zeta(5, "#A302EE");

        private int mPosition;
        private String mRgb;

        TYPE(int position, String rgb) {
            mPosition = position;
            mRgb = rgb;
        }

        public int getPosition() {
            return mPosition;
        }

        public String getRgb() {
            return mRgb;
        }
    }
}
