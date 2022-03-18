package com.bol.mancala.service;

import com.bol.mancala.exception.InputInvalidException;
import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.repository.MancalaGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static com.bol.mancala.common.Players.PLAYER_A;
import static com.bol.mancala.common.Players.PLAYER_B;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MancalaGameServiceTest {
    private final Logger logger = LoggerFactory.getLogger(MancalaGameServiceTest.class);
    private MancalaGame game;
    @Autowired
    private MancalaGameService gameService;
    @Autowired
    private MancalaGameRepository gameRepository;


    @BeforeEach
    void setup() {
        Integer pitStonesCount = 6;
        this.game = this.gameService.getNewGame(pitStonesCount);
        assertNotNull(this.game);
        assertEquals(this.game, this.gameService.getGame(this.game.getId()));
    }

    @Test
    @DisplayName(value = "Test the repository to make sure it gets updated when a new game is created")
    void testRepositoryWhenNewGameCreated() {
        assertEquals(this.game, this.gameRepository.get(this.game.getId()));
    }

    @Test
    @DisplayName("Testing when PlayerA selects his first pit")
    void testPlayGame() {

        assertEquals(6, this.game.getPit(1).getStones());

        // in this case, the last stone lands in his big(playerA) pit, so he will have another chance to sow again.
        assertEquals("[{id=1, stones=0}, {id=2, stones=7}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=6}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 1).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // the second time when PlayerA want to choose an empty pit which selected before, so an instance of game without change must be returned
        assertEquals("[{id=1, stones=0}, {id=2, stones=7}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=6}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 1).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=0}, {id=2, stones=0}, {id=3, stones=8}, {id=4, stones=8}, {id=5, stones=8}, {id=6, stones=8}, {id=7, stones=2}, {id=8, stones=7}, {id=9, stones=7}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

    }


    @Test
    @DisplayName("Testing when PlayerB selects his second pit")
    void testWhenPlayerBSelectsHisSecondPit() {
        assertEquals("[{id=1, stones=7}, {id=2, stones=6}, {id=3, stones=6}, {id=4, stones=6}, {id=5, stones=6}, {id=6, stones=6}, {id=7, stones=0}, {id=8, stones=6}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", this.gameService.playGame(this.game.getId(), 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // when playerA select a wrong pit to start with (pit number8). an Instance game without change must be returned
        assertEquals("[{id=1, stones=7}, {id=2, stones=6}, {id=3, stones=6}, {id=4, stones=6}, {id=5, stones=6}, {id=6, stones=6}, {id=7, stones=0}, {id=8, stones=6}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", this.gameService.playGame(this.game.getId(), 8).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());
    }

    @Test
    @DisplayName("Testing when PlayerA's last stone lands in his empty pit")
    void TestWhenPlayerLastPitPlacesInEmptyPit() {
        assertEquals("[{id=1, stones=6}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=7}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=0}, {id=10, stones=7}, {id=11, stones=7}, {id=12, stones=7}, {id=13, stones=7}, {id=14, stones=1}]", this.gameService.playGame(this.game.getId(), 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=7}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=0}, {id=7, stones=2}, {id=8, stones=8}, {id=9, stones=1}, {id=10, stones=8}, {id=11, stones=8}, {id=12, stones=8}, {id=13, stones=8}, {id=14, stones=1}]", this.gameService.playGame(this.game.getId(), 6).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=8}, {id=4, stones=8}, {id=5, stones=8}, {id=6, stones=1}, {id=7, stones=2}, {id=8, stones=9}, {id=9, stones=1}, {id=10, stones=8}, {id=11, stones=8}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", this.gameService.playGame(this.game.getId(), 13).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=3}, {id=8, stones=10}, {id=9, stones=2}, {id=10, stones=9}, {id=11, stones=9}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", this.gameService.playGame(this.game.getId(), 3).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertEquals("[{id=1, stones=8}, {id=2, stones=1}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=3}, {id=8, stones=10}, {id=9, stones=0}, {id=10, stones=10}, {id=11, stones=10}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", this.gameService.playGame(this.game.getId(), 9).getPits().toString());
        assertEquals(PLAYER_A, this.game.getPlayerTurn());

        // this is time when playerA's last stone lands in his empty pit(pit number3)
        assertEquals("[{id=1, stones=8}, {id=2, stones=0}, {id=3, stones=0}, {id=4, stones=9}, {id=5, stones=9}, {id=6, stones=2}, {id=7, stones=14}, {id=8, stones=10}, {id=9, stones=0}, {id=10, stones=10}, {id=11, stones=0}, {id=12, stones=8}, {id=13, stones=0}, {id=14, stones=2}]", this.gameService.playGame(this.game.getId(), 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());
    }


    @Test
    @DisplayName("Test when a player chooses a wrong pitIndex")
    void testWhenBigPitChosenToSow() {
        assertEquals("[{id=1, stones=6}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        assertThrows(InputInvalidException.class, () -> this.gameService.playGame(this.game.getId(), 17).getPits().toString());

    }

    @Test
    @DisplayName(value = "Test when gameId is invalid")
    void testWhenInvalidPitId() {
        assertThrows(ResourceNotFoundException.class, () -> this.gameService.playGame("", 1));
    }

    @Test
    @DisplayName(value = "Test when a player chooses his own big pit to start sowing")
    void testWhenSelectingBigPitForSowing() {
        assertEquals("[{id=1, stones=6}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 2).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());

        // playerB chooses his big pit to start sowing with
        assertEquals("[{id=1, stones=6}, {id=2, stones=0}, {id=3, stones=7}, {id=4, stones=7}, {id=5, stones=7}, {id=6, stones=7}, {id=7, stones=1}, {id=8, stones=7}, {id=9, stones=6}, {id=10, stones=6}, {id=11, stones=6}, {id=12, stones=6}, {id=13, stones=6}, {id=14, stones=0}]", this.gameService.playGame(this.game.getId(), 14).getPits().toString());
        assertEquals(PLAYER_B, this.game.getPlayerTurn());
    }

    @Test
    @DisplayName(value = "Test when all six pits of a player are empty (a complete game)")
    void testWhenAlLSixPitsEmpty() {
        Random random = new Random();
        int stonesCountWinner;
        int stonesCountLoser;
        int index = 1;

        while (this.game.getWinner() == null) {
            if (game.getPlayerTurn() == PLAYER_A) {
                index = random.nextInt(7 - 1) + 1;
            }
            if (game.getPlayerTurn() == PLAYER_B) {
                index = random.nextInt(14 - 8) + 8;
            }
            logger.info("pitIndex selected: {}", index);
            this.gameService.playGame(this.game.getId(), index);
        }

        if (this.game.getWinner().equals(PLAYER_A.name())) {
            stonesCountWinner = this.game.getPit(7).getStones();
            stonesCountLoser = this.game.getPit(14).getStones();
        } else {
            stonesCountWinner = this.game.getPit(14).getStones();
            stonesCountLoser = this.game.getPit(7).getStones();
        }

        assertNotNull(this.game.getWinner());
        assertEquals(72, this.game.getPit(7).getStones() + this.game.getPit(14).getStones());

        logger.info("winner is {} with {} stones and loser with {} stones"
                , this.game.getWinner()
                , stonesCountWinner
                , stonesCountLoser);
    }
}
