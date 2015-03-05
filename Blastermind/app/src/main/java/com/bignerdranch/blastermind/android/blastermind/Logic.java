package com.bignerdranch.blastermind.android.blastermind;

public class Logic {

    public enum TYPE {
        Red(0),
        Green(1),
        Blue(2),
        Yellow(3),
        Purple(4),
        Orange(5);

        private int mPosition;

        TYPE(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }
    }
}
