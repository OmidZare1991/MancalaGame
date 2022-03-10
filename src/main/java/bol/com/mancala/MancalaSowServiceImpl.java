package bol.com.mancala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class MancalaSowServiceImpl implements MancalaGameService<MancalaGame, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaSowServiceImpl.class);

    @Autowired
    private SowService sowService;

    /**
     * @param game     MancalaGame instance
     * @param pitIndex performing sowing the game on this requested pitIndex
     * @return returning MancalaGame instance
     */
    @Override
    public MancalaGame sow(MancalaGame game, Integer pitIndex) {
        // No movement on big pits
        if (pitIndex == MancalaConstants.RIGHT_BIG_PIT_ID || pitIndex == MancalaConstants.LEFT_BIG_PIT_ID) {
            LOGGER.debug("no movement on big pits, id {}", pitIndex);
            return game;
        }

        // setting the player turn for the first move of the game based on the requested pit id
        if (game.getPlayerTurn() == null) {

            if (pitIndex < MancalaConstants.RIGHT_BIG_PIT_ID) {
                LOGGER.debug("Player {} sows", Players.PLAYER_A);
                game.setPlayerTurn(Players.PLAYER_A);
            } else {
                LOGGER.debug("Player {} sows", Players.PLAYER_B);
                game.setPlayerTurn(Players.PLAYER_B);
            }
        }

        // check if the player chose the correct pit index
        if (game.getPlayerTurn() == Players.PLAYER_A && pitIndex > MancalaConstants.RIGHT_BIG_PIT_ID ||
                game.getPlayerTurn() == Players.PLAYER_B && pitIndex < MancalaConstants.RIGHT_BIG_PIT_ID) {
            LOGGER.debug("Player {} chose the wrong pitIndex {} ", game.getPlayerTurn(), pitIndex);
            return game;
        }

        Pit mancalaSelectedPit = game.getPit(pitIndex);

        Integer stones = mancalaSelectedPit.getStones();
        // no movement for the empty pit
        if (MancalaConstants.EMPTY_STONE == stones) {
            LOGGER.debug("pit with pitIndex {} is empty", pitIndex);
            return game;
        }

        // the selected pit must be empty after the stones are got off
        mancalaSelectedPit.setStones(MancalaConstants.EMPTY_STONE);

        // keep the pit index, used for sowing the stones in right pits
        game.setCurrentPitIndex(pitIndex);

        //sow all stones except the last one
        IntStream.range(0, stones - 1).forEach(index -> sowService.sowRight(game, false));

        sowService.sowRight(game, true);

        int currentPitIndex = game.getCurrentPitIndex();

        // change the player's turn if the last sow was not on any of pit big pits (left or right)
        if (currentPitIndex != MancalaConstants.RIGHT_BIG_PIT_ID && currentPitIndex != MancalaConstants.LEFT_BIG_PIT_ID)
            game.setPlayerTurn(changeTurns(game.getPlayerTurn()));

        return game;
    }

    private Players changeTurns(Players currentTurn) {
        return currentTurn == Players.PLAYER_A ? Players.PLAYER_B : Players.PLAYER_A;
    }
}
