package com.bignerdranch.blastermind.android.core;

import com.bignerdranch.blastermind.andorid.core.Player;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    @Test
    public void playerShouldHaveName() throws Exception {
        Player player = new Player("test name");
        assertThat(player.getName()).isEqualTo("test name");
    }

    @Test
    public void playerShouldHaveId() throws Exception {
        Player player = new Player("test name");
        player.setId(1234);
        assertThat(player.getId()).isEqualTo(1234);
    }
}
