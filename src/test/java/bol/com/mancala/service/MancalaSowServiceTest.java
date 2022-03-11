package bol.com.mancala.service;

import bol.com.mancala.GameModel;
import bol.com.mancala.MancalaSowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static bol.com.mancala.Players.PLAYER_A;
import static bol.com.mancala.Players.PLAYER_B;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MancalaSowServiceTest {
    private GameModel game;
    private String id;
    @Autowired
    private MancalaSowServiceImpl sowService;


    @BeforeEach
    void setup() {
        this.id = UUID.randomUUID().toString();
        this.game = new GameModel(id, 6);
        System.out.println(this.game.getPits());
    }

    @Test
    void testGameInstance() {
        assertNotNull(this.game);
    }

    @Test
    @DisplayName("Testing if PlayerA selects his first pit")
    void testWhenPlayerASelectsFirstPit() {
        // in this case, the last stone lands in his big(playerA) pit, so he will have another chance to start.
        assertEquals("[{id=1, stones=0}, {id=2, stones=7}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=6}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", sowService.sow(this.game, 1).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // the second time when PlayerA want to choose an empty pit which selected before, so an instance of game without change must be returned
        assertEquals("[{id=1, stones=0}, {id=2, stones=7}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=6}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", sowService.sow(this.game, 1).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=0}, {id=2, stones=0}, {id=3, stones=8}, {id=4, stones=8}, {id=5, stones=8}, {id=6, stones=8}, {id=7, stones=2}, {id=8, stones=7}, {id=9, stones=7}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.sowService.sow(this.game, 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());
    }

    @Test
    @DisplayName("Testing if PlayerB selects his second pit")
    void testWhenPlayerBSelectsHisSecondPit() {
        assertEquals("[{id=1, stones=7}, {id=2, stones=6}, {id=3, stones=6}, {id=4, stones=6}, {id=5, stones=6}, {id=6, stones=6}, {id=7, stones=0}, {id=8, stones=6}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", sowService.sow(this.game, 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // when playerA select a wrong pit to start with (pit number8). an Instance game without change must be returned
        assertEquals("[{id=1, stones=7}, {id=2, stones=6}, {id=3, stones=6}, {id=4, stones=6}, {id=5, stones=6}, {id=6, stones=6}, {id=7, stones=0}, {id=8, stones=6}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", sowService.sow(this.game, 8).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());
    }

    @Test
    @DisplayName("Testing if PlayerA's last stone lands in his empty pit")
    void TestWhenPlayerLastPitPlacesInEmptyPit() {
        assertEquals("[{id=1, stones=6}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", sowService.sow(this.game, 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=7}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", sowService.sow(this.game, 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=7}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=0}, {id=7, stones=2}, {id=8, stones=8}, {id=9, stones=1}, {id=10, stones=8}, {id=11, stones=8}, {id=12, stones=8}, {id=13, stones=8}, {id=14, stones=1}]", sowService.sow(this.game, 6).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=8}, {id=4, stones=8}, {id=5, stones=8}, {id=6, stones=1}, {id=7, stones=2}, {id=8, stones=9}, {id=9, stones=1}, {id=10, stones=8}, {id=11, stones=8}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", sowService.sow(this.game, 13).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=3}, {id=8, stones=10}, {id=9, stones=2}, {id=10, stones=9}, {id=11, stones=9}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", sowService.sow(this.game, 3).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=3}, {id=8, stones=10}, {id=9, stones=0}, {id=10, stones=10}, {id=11, stones=10}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", sowService.sow(this.game, 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // this is time when playerA's last stone lands in his empty pit(pit number3)
        assertEquals("[{id=1, stones=8}, {id=2, stones=0}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=14}, {id=8, stones=10}, {id=9, stones=0}, {id=10, stones=10}, {id=11, stones=0}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", sowService.sow(this.game, 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());
    }

    @Test
    @DisplayName("Test when a player chooses his big pit to start sowing")
    void testWhenBigPitChosenToSow() {
        String game = "[{id=1, stones=6}, {id=2, stones=6}, {id=3, stones=6}, {id=4, stones=6}, {id=5, stones=6}, {id=6, stones=6}, {id=7, stones=0}, {id=8, stones=6}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]";
        assertEquals(game, sowService.sow(this.game, 7).getPits().toString());
        // because it is the start of game and playerA wants to start the game, game.PlayerTurn() is null.
        assertEquals(null, this.game.getPlayerTurn());
        assertEquals(game, this.game.getPits().toString());
    }

}
