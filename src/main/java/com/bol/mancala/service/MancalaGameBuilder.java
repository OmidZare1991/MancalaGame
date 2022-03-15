package com.bol.mancala.service;

import com.bol.mancala.model.BigPit;
import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.model.Pit;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.bol.mancala.common.MancalaConstants.*;

@Service
public class MancalaGameBuilder {
    /**
     * @param stonesCount the number of stones to initialize the game with
     * @return the object of game
     */
    public MancalaGame getNew(Integer stonesCount) {
        return this.createInstance(UUID.randomUUID().toString(), stonesCount);
    }

    /**
     * @param id          the game id generated when initializing the game
     * @param stonesCount the number of stones to initialize the game with
     * @return the object of game
     */
    private MancalaGame createInstance(String id, Integer stonesCount) {
        List<Pit> pits = Arrays.asList(
                new Pit(FIRST_PIT_PLAYER_A, stonesCount),
                new Pit(SECOND_PIT_PLAYER_A, stonesCount),
                new Pit(THIRD_PIT_PLAYER_A, stonesCount),
                new Pit(FORTH_PIT_PLAYER_A, stonesCount),
                new Pit(FIFTH_PIT_PLAYER_A, stonesCount),
                new Pit(SIXTH_PIT_PLAYER_A, stonesCount),
                new BigPit(RIGHT_BIG_PIT_ID),
                new Pit(FIRST_PIT_PLAYER_B, stonesCount),
                new Pit(SECOND_PIT_PLAYER_B, stonesCount),
                new Pit(THIRD_PIT_PLAYER_B, stonesCount),
                new Pit(FORTH_PIT_PLAYER_B, stonesCount),
                new Pit(FIFTH_PIT_PLAYER_B, stonesCount),
                new Pit(SIXTH_PIT_PLAYER_B, stonesCount),
                new BigPit(LEFT_BIG_PIT_ID));
        return new MancalaGame(id, pits);
    }
}
