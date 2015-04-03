package com.bignerdranch.blastermind.andorid.core;

public class Logic {

    // number of types in a guess
    public static final int guessWidth = 4;
    public static final int guessLimit = 10;

    public enum TYPE {
        alpha(0),
        beta(1),
        gamma(2),
        delta(3),
        epsilon(4),
        zeta(5);

        private int mPosition;

        TYPE(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }
    }
}
