package bol.com.mancala.service;

import bol.com.mancala.MancalaGame;
import bol.com.mancala.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class MancalaGameServiceTest {
    @Autowired
    GameService mancalaGameService;

    @Test
    void testMancalaGameService() {
        String id = UUID.randomUUID().toString();
        MancalaGame gameInstance = mancalaGameService.createInstance(id, 6);


        assertNull(gameInstance.getPlayerTurn());
        assertEquals(id, gameInstance.getId());
        assertEquals(0, gameInstance.getCurrentPitIndex());

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

