package com.tir38.android.blastermind.core;

import com.tir38.android.blastermind.core.Guess;
import com.tir38.android.blastermind.core.Logic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GuessTest {

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Test
    public void guessMustHaveTypes() throws Exception {
        // setup
        List<Logic.TYPE> expectedTypes = new ArrayList<>(Logic.guessWidth);
        expectedTypes.add(Logic.TYPE.alpha);
        expectedTypes.add(Logic.TYPE.alpha);
        expectedTypes.add(Logic.TYPE.alpha);
        expectedTypes.add(Logic.TYPE.alpha);
        Guess guess = new Guess(expectedTypes);

        // validate
        List<Logic.TYPE> actualTypes = guess.getTypes();
        assertThat(actualTypes.size()).isEqualTo(Logic.guessWidth);
    }

    @Test
    public void guessCantHaveTooFewTypes() throws Exception {
        mException.expect(IllegalArgumentException.class);
        List<Logic.TYPE> types = new ArrayList<>(Logic.guessWidth - 1);
        new Guess(types);
    }

    @Test
    public void guessCantHaveTooManyTypes() throws Exception {
        mException.expect(IllegalArgumentException.class);
        List<Logic.TYPE> types = new ArrayList<>(Logic.guessWidth + 1);
        new Guess(types);
    }
}
