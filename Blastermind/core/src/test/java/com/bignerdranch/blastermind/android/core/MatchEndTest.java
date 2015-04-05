package com.bignerdranch.blastermind.android.core;

import com.bignerdranch.blastermind.andorid.core.Guess;
import com.bignerdranch.blastermind.andorid.core.Logic;
import com.bignerdranch.blastermind.andorid.core.MatchEnd;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchEndTest {

    private MatchEnd mMatchEnd;

    @Before
    public void setUp() throws Exception {
        mMatchEnd = new MatchEnd();
    }

    @Test
    public void matchEndHasMatchId() throws Exception {
        mMatchEnd.setMatchId(123);
        assertThat(mMatchEnd.getMatchId()).isEqualTo(123);
    }

    @Test
    public void matchEndHasWinnerId() throws Exception {
        mMatchEnd.setWinnerId(2345);
        assertThat(mMatchEnd.getWinnerId()).isEqualTo(2345);
    }

    @Test
    public void matchEndHasWinnerName() throws Exception {
        mMatchEnd.setWinnerName("winner name");
        assertThat(mMatchEnd.getWinnerName()).isEqualTo("winner name");
    }

    @Test
    public void matchEndHasSolution() throws Exception {
        List<Logic.TYPE> types = new ArrayList<>();
        types.add(Logic.TYPE.alpha);
        types.add(Logic.TYPE.beta);
        types.add(Logic.TYPE.delta);
        types.add(Logic.TYPE.gamma);
        Guess solution = new Guess(types);
        mMatchEnd.setSolution(solution);
        assertThat(mMatchEnd.getSolution().getTypes()).isEqualTo(types);
    }
}
