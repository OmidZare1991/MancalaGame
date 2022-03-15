package com.bol.mancala.service;

import com.bol.mancala.common.ErrorConstants;
import com.bol.mancala.common.Players;
import com.bol.mancala.exception.InputInvalidException;
import com.bol.mancala.exception.ResourceNotFoundException;
import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.model.Pit;
import com.bol.mancala.repository.MancalaGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bol.mancala.common.ErrorConstants.INPUT_INVALID_EXCEPTION_MESSAGE;
import static com.bol.mancala.common.MancalaConstants.*;
import static com.bol.mancala.common.Players.PLAYER_A;
import static com.bol.mancala.common.Players.PLAYER_B;

@Service
public class MancalaGameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MancalaGameService.class);

    @Autowired
    private MancalaGameBuilder gameBuilder;

    @Autowired
    private MancalaGameRepository gameRepository;

    /**
     * @param pitStonesCount the number of stones to start game with
     * @return the object of game with the pits filled with the number of 'pitStonesCount' stones
     */
    public MancalaGame getNewGame(Integer pitStonesCount) {
        MancalaGame game = gameBuilder.getNew(pitStonesCount);
        this.gameRepository.update(game);
        return game;
    }

    /**
     * @param gameId the id of game generated when the game started
     * @return the object of game
     */
    public MancalaGame getGame(String gameId) {
        return this.gameRepository.get(gameId);
    }

    /**
     * @param gameId the id of game created by the app when game started
     * @param pitId  the id of selected pit to start sowing from
     * @return the object of Mancala game
     */
    public MancalaGame playGame(String gameId, Integer pitId) {
        // get the last object of game (last state) from the repository
        MancalaGame game = gameRepository.get(gameId);
        if (null == game) {
            LOGGER.error("game with id {} not found", gameId);
            throw new ResourceNotFoundException(String.format(ErrorConstants.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE, gameId));
        }
        if (pitId == null ||
                pitId < 1 || pitId > LEFT_BIG_PIT_ID
        ) {
            LOGGER.error("input {} is not valid", pitId);
            throw new InputInvalidException(String.format(INPUT_INVALID_EXCEPTION_MESSAGE, pitId));
        }
//        game = this.sow(game, pitId);

        this.sow(game, pitId);
        // update the repository when sowing is finished to keep the last state of game for next round
        gameRepository.update(game);
        return game;
    }

    /**
     * @param game     MancalaGame object
     * @param pitIndex The requested pitIndex which the player selected to start sowing from
     * @return returning MancalaGame instance
     */
    private MancalaGame sow(MancalaGame game, Integer pitIndex) {
        // No movement on big pits
        if (pitIndex == RIGHT_BIG_PIT_ID || pitIndex == LEFT_BIG_PIT_ID) {
            LOGGER.info("no movement on big pits, id {}", pitIndex);
            return game;
        }

        // setting the player turn based on the selected pitId when game is started by one the players
        if (game.getPlayerTurn() == null) {

            if (pitIndex < RIGHT_BIG_PIT_ID) {
                LOGGER.info("Player {} sows", PLAYER_A);
                game.setPlayerTurn(PLAYER_A);
            } else {
                LOGGER.info("Player {} sows", PLAYER_B);
                game.setPlayerTurn(PLAYER_B);
            }
        }

        // check if the player chose the correct pit index
        if (game.getPlayerTurn() == PLAYER_A && pitIndex > RIGHT_BIG_PIT_ID ||
                game.getPlayerTurn() == PLAYER_B && pitIndex < RIGHT_BIG_PIT_ID) {
            LOGGER.info("Player {} chose the wrong pitIndex {} ", game.getPlayerTurn(), pitIndex);
            return game;
        }

        Pit mancalaSelectedPit = game.getPit(pitIndex);

        Integer stones = mancalaSelectedPit.getStones();
        // no movement for the empty pit
        if (EMPTY_STONE == stones) {
            LOGGER.info("pit with pitIndex {} is empty", pitIndex);
            return game;
        }

        // the selected pit stones is set to empty when the player selects the pit to start sowing
        mancalaSelectedPit.setStones(EMPTY_STONE);

        // keep the current pit index, needed for sowing the stones in right pits
        game.setCurrentPitIndex(pitIndex);

        //sow all stones except the last one
        IntStream.range(0, stones - 1).forEach(index -> this.sowRight(game, false));

        this.sowRight(game, true);

        int currentPitIndex = game.getCurrentPitIndex();

        // change the player's turn if the last stone does not lands on the current player's pit
        if (currentPitIndex != RIGHT_BIG_PIT_ID && currentPitIndex != LEFT_BIG_PIT_ID)
            game.setPlayerTurn(changeTurns(game.getPlayerTurn()));

        // check if game is finished or not. if so, returning the name of winner in game object
        return getGameState(game);
    }

    /**
     * @param game        MancalaGame object
     * @param isLastStone the boolean value representing if it is the last sowing stone
     */
    private void sowRight(MancalaGame game, boolean isLastStone) {

        // the pitIndex to start sowing from
        int currentPitIndex = game.getCurrentPitIndex() % TOTAL_PITS + 1;

        Players player = game.getPlayerTurn();

        //no stones are put in the opponent's big pit
        if ((currentPitIndex == RIGHT_BIG_PIT_ID && player == PLAYER_B) ||
                (currentPitIndex == LEFT_BIG_PIT_ID && player == PLAYER_A)) {

            currentPitIndex = currentPitIndex % TOTAL_PITS + 1;
        }
        game.setCurrentPitIndex(currentPitIndex);
        Pit pit = game.getPit(currentPitIndex);


        // it is not the last stone and it lands on the player's middle pits or big pit
        // it is the last stone but it lands on the player's big pit
        if (!isLastStone || currentPitIndex == RIGHT_BIG_PIT_ID ||
                currentPitIndex == LEFT_BIG_PIT_ID) {
            pit.sow();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status
        // we need this pit's stones count to be added to the player's pit if the last stone
        //lands on the player's empty pit
        Pit oppositePit = game.getPit(TOTAL_PITS - currentPitIndex);


        //when the last stone lands in an own empty pit,the player captures his last stone and all stones in the opposite pit
        //and puts them in his big pit
        if (pit.isEmpty() && isOwnEmptyPit(pit.getId(), player)) {

            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer bigPitIndex = currentPitIndex < RIGHT_BIG_PIT_ID ? RIGHT_BIG_PIT_ID : LEFT_BIG_PIT_ID;
            Pit bigPit = game.getPit(bigPitIndex);
            bigPit.addStones(1 + oppositeStones);
            return;
        }

        // when it is the last stone and it lands on the opponent's (empty  or non empty) pit
        pit.sow();
    }


    /**
     * @param id      the index of empty pit in which last stone landed
     * @param players the current player
     * @return boolean value showing if the empty pit is the current player's pit, not the opponent's
     */
    private boolean isOwnEmptyPit(int id, Players players) {
        return (players == PLAYER_A && id < 7) || (players == PLAYER_B && id > 7 && id < 14);
    }

    /**
     * @param currentTurn Player Enum showing the current player's turn
     * @return change the turn to the next player
     */
    private Players changeTurns(Players currentTurn) {
        return currentTurn == PLAYER_A ? PLAYER_B : PLAYER_A;
    }


    /**
     * @param game MancalaGame object
     * @return if the game is not finished, an object of game without change is returned
     * if the game is finished, the stones in players' big pits are counted to check which player is the winner
     * Then,the game object showing the winner (instance variable 'win' set to winner's name) is returned
     */
    private MancalaGame getGameState(MancalaGame game) {


        //check if all playerA's six pits are empty
        boolean playerAPits = isAllSixPitsEmpty(FIRST_PIT_PLAYER_A, SIXTH_PIT_PLAYER_A, game);

        //check if all playerB's six pits are empty
        boolean playerBPits = isAllSixPitsEmpty(FIRST_PIT_PLAYER_B, SIXTH_PIT_PLAYER_B, game);


        // if no player finished sowing
        if (!playerAPits && !playerBPits) {
            return game;
        }
        // if playerA finished sowing
        if (playerAPits) {
            for (int i = FIRST_PIT_PLAYER_B; i <= SIXTH_PIT_PLAYER_B; i++) {
                game.getPit(LEFT_BIG_PIT_ID).setStones(game.getPit(LEFT_BIG_PIT_ID).getStones() + game.getPit(i).getStones());
            }
        }
        //if playerB finished sowing
        if (playerBPits) {
            for (int i = FIRST_PIT_PLAYER_A; i <= SIXTH_PIT_PLAYER_A; i++) {
                game.getPit(RIGHT_BIG_PIT_ID).setStones(game.getPit(RIGHT_BIG_PIT_ID).getStones() + game.getPit(i).getStones());
            }
        }

        //check which player won the game
        if (game.getPit(RIGHT_BIG_PIT_ID).getStones() > game.getPit(LEFT_BIG_PIT_ID).getStones()) {
            game.setWin(PLAYER_A.name());
        } else {
            game.setWin(PLAYER_B.name());
        }
        return game;
    }


    /**
     * @param startPoint the index of current player's first pit
     * @param endPoint   the index of curent player's last pit
     * @param game       MancalaGame object
     * @return boolean value showing if all six pits of that player are empty
     */
    private boolean isAllSixPitsEmpty(int startPoint, int endPoint, MancalaGame game) {
        // a predicate of type Pit and use lambda expression to check that
        // all player's six pits are empty (have no stones) for each player
        Predicate<Pit> playerPits = pit -> (pit.getId() >= startPoint && pit.getId() <= endPoint && pit.isEmpty());

        // comparing the number of empty pits of either player with number 6
        // to check if all player's six pits are empty
        return game.getPits()
                .stream()
                .filter(playerPits)
                .collect(Collectors.toList()).size() == 6;

    }

}