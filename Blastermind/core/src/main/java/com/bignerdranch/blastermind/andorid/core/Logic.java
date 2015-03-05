package com.bignerdranch.blastermind.andorid.core;

public class Logic {

    // number of types in a guess
    public static final int guessWidth = 4;
    public static final int guessLimit = 10;

    public enum TYPE {
        Red(0, "#FF0000"),
        Green(1, "#00FF00"),
        Blue(2, "#0000FF"),
        Yellow(3, "#FFFF00"),
        Purple(4, "#800080"),
        Orange(5, "#FFA500");

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
