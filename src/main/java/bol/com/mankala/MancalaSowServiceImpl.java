package bol.com.mankala;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class MancalaSowServiceImpl implements MancalaSowApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaSowServiceImpl.class);

    /**
     * @param game     MancalaGame instance
     * @param pitIndex performing sowing the game on this requested pitIndex
     * @return returning MancalaGame instance
     */
    @Override
    public MancalaGame sow(MancalaGame game, int pitIndex) {
        // No movement on Big pits
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

        MancalaPit mancalaSelectedPit = game.getPit(pitIndex);

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
        IntStream.range(0, stones - 1).forEach(index -> sowRight(game, false));
        int currentPitIndex = game.getCurrentPitIndex();

        // change the player's turn if the last sow was not on any of pit big pits (left or right)
        if (currentPitIndex != MancalaConstants.RIGHT_BIG_PIT_ID && currentPitIndex != MancalaConstants.LEFT_BIG_PIT_ID)
            game.setPlayerTurn(changeTurns(game.getPlayerTurn()));

        return game;
    }

    public void sowRight(MancalaGame game, boolean isLastStone) {

        // the pitIndex to start sowing from
        int currentPitIndex = game.getCurrentPitIndex() % MancalaConstants.TOTAL_PITS + 1;

        Players player = game.getPlayerTurn();

        //no stones are put in the opponent's big pit
        if ((currentPitIndex == MancalaConstants.RIGHT_BIG_PIT_ID && player == Players.PLAYER_B) ||
                (currentPitIndex == MancalaConstants.LEFT_BIG_PIT_ID && player == Players.PLAYER_A)) {

            currentPitIndex = currentPitIndex % MancalaConstants.TOTAL_PITS + 1;
            game.setCurrentPitIndex(currentPitIndex);
        }
        MancalaPit pit = game.getPit(currentPitIndex);

        if (!isLastStone || currentPitIndex == MancalaConstants.RIGHT_BIG_PIT_ID ||
                currentPitIndex == MancalaConstants.LEFT_BIG_PIT_ID) {
            pit.sow();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status

        MancalaPit oppositePit = game.getPit(MancalaConstants.TOTAL_PITS - currentPitIndex);

        //when the last stone lands in an own empty pit,the player captures his own stones and all stones in the opposite pit
        //and puts them in his big pit
        if (pit.isEmpty() && !oppositePit.isEmpty()) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer bigPitIndex = currentPitIndex < MancalaConstants.RIGHT_BIG_PIT_ID ? MancalaConstants.RIGHT_BIG_PIT_ID : MancalaConstants.LEFT_BIG_PIT_ID;
            MancalaPit bigPit = game.getPit(bigPitIndex);
            bigPit.addStones(oppositeStones + 1);
            return;
        }
        pit.sow();
    }

    private Players changeTurns(Players currentTurn) {
        return currentTurn == Players.PLAYER_A ? Players.PLAYER_B : Players.PLAYER_A;
    }

}
