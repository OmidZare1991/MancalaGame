package com.bol.mancala.service;

import com.bol.mancala.model.MancalaGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class MancalaGameBuilderTest {
    @Autowired
    MancalaGameBuilder gameBuilder;

    @Test
    @DisplayName(value = "Test when game instance is created")
    void testMancalaGameBuilder() {
        Integer pitStonesCount = 6;
        MancalaGame gameInstance = gameBuilder.getNew(pitStonesCount);


        assertNull(gameInstance.getPlayerTurn());

        assertNull(gameInstance.getCurrentPitIndex());
        assertEquals(6, gameInstance.getPit(1).getStones());
        assertEquals(6, gameInstance.getPit(2).getStones());
        assertEquals(6, gameInstance.getPit(3).getStones());
        assertEquals(6, gameInstance.getPit(4).getStones());
        assertEquals(6, gameInstance.getPit(5).getStones());
        assertEquals(6, gameInstance.getPit(6).getStones());
        assertEquals(0, gameInstance.getPit(7).getStones());
        assertEquals(6, gameInstance.getPit(8).getStones());
        assertEquals(6, gameInstance.getPit(9).getStones());
        assertEquals(6, gameInstance.getPit(10).getStones());
        assertEquals(6, gameInstance.getPit(11).getStones());
        assertEquals(6, gameInstance.getPit(12).getStones());
        assertEquals(6, gameInstance.getPit(13).getStones());
        assertEquals(0, gameInstance.getPit(14).getStones());

    }
}

