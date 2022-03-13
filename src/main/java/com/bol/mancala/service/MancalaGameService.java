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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    public MancalaGame getNewGame(Integer pitStonesCount) {
        MancalaGame game = gameBuilder.getNew(pitStonesCount);
        this.gameRepository.update(game);
        return game;
    }

    public MancalaGame getGame(String gameId) {
        return this.gameRepository.get(gameId);
    }

    public MancalaGame playGame(String gameId, Integer pitId) {
        MancalaGame game = gameRepository.get(gameId);
        if (null == game) {
            LOGGER.error("game with id {} not found", gameId);
            throw new ResourceNotFoundException(String.format(ErrorConstants.RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE, gameId));
        }
        if (pitId == null ||
                pitId < 1 || pitId > LEFT_BIG_PIT_ID
//                || pitId == MancalaConstants.RIGHT_BIG_PIT_ID
        ) {
            throw new InputInvalidException(String.format(ErrorConstants.INPUT_INVALID_EXCEPTION_MESSAGE, pitId));
        }
        game = this.sow(game, pitId);
//        this.sow(game, pitId);
        gameRepository.update(game);
        return game;
    }

    /**
     * @param game     MancalaGame instance
     * @param pitIndex performing sowing the game on this requested pitIndex
     * @return returning MancalaGame instance
     */
    private MancalaGame sow(MancalaGame game, Integer pitIndex) {
        // No movement on big pits
        if (pitIndex == RIGHT_BIG_PIT_ID || pitIndex == LEFT_BIG_PIT_ID) {
            LOGGER.info("no movement on big pits, id {}", pitIndex);
            return game;
        }

        // setting the player turn for the first move of the game based on the requested pit id
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

        // the selected pit must be empty after the stones are got off
        mancalaSelectedPit.setStones(EMPTY_STONE);

        // keep the pit index, used for sowing the stones in right pits
        game.setCurrentPitIndex(pitIndex);

        //sow all stones except the last one
        IntStream.range(0, stones - 1).forEach(index -> this.sowRight(game, false));

        this.sowRight(game, true);

        int currentPitIndex = game.getCurrentPitIndex();

        // change the player's turn if the last sow was not on any of pit big pits (left or right)
        if (currentPitIndex != RIGHT_BIG_PIT_ID && currentPitIndex != LEFT_BIG_PIT_ID)
            game.setPlayerTurn(changeTurns(game.getPlayerTurn()));

        // check if game is finished or not. if so, returning the name of winner in game object
        return getFinalGameState(game);
//        return game;
    }

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

        // it's not the last stone or it is either of big pits' index ( (!isLastStone means the condition is ok
        // for all indexes except big pits, and the other two conditions  checks for the big ones)
        // when it is not the last stone, three possible conditions occur(1. it lands on either of big pits,
        // conditions 2 and 3 will be checked in next lines of code after this if condition)
        if (!isLastStone || currentPitIndex == RIGHT_BIG_PIT_ID ||
                currentPitIndex == LEFT_BIG_PIT_ID) {
            pit.sow();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status

        Pit oppositePit = game.getPit(TOTAL_PITS - currentPitIndex);


        //when the last stone lands in an own empty pit,the player captures his own stones and all stones in the opposite pit
        //and puts them in his big pit
        if (pit.isEmpty()) {

            if (isOwnPitEmpty(pit.getId(), player)) {
                Integer oppositeStones = oppositePit.getStones();
                oppositePit.clear();
                Integer bigPitIndex = currentPitIndex < RIGHT_BIG_PIT_ID ? RIGHT_BIG_PIT_ID : LEFT_BIG_PIT_ID;
                Pit bigPit = game.getPit(bigPitIndex);
                bigPit.addStones(oppositeStones + 1);
                return;
            }
        }

        // when it is the last stone and it lands on the opponent's empty pit
        pit.sow();
    }

    private boolean isOwnPitEmpty(int id, Players players) {
        if ((players == PLAYER_A && id < 7) || (players == PLAYER_B && id > 7 && id < 14)) {
            return true;
        }
        return false;
    }

    private Players changeTurns(Players currentTurn) {
        return currentTurn == PLAYER_A ? PLAYER_B : PLAYER_A;
    }

    public MancalaGame getFinalGameState(MancalaGame game) {


        boolean playerAPits = isAllPitsEmpty(FIRST_PIT_PLAYER_A, SIXTH_PIT_PLAYER_A, game);
        boolean playerBPits = isAllPitsEmpty(FIRST_PIT_PLAYER_B, SIXTH_PIT_PLAYER_B, game);


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

    private boolean isAllPitsEmpty(int startPoint, int endPoint, MancalaGame game) {
        // a predicate checking that pits are empty (have no stones) for each player
        Predicate<Pit> playerPits = pit -> (pit.getId() >= startPoint && pit.getId() <= endPoint && pit.isEmpty());

        // get the list of all pits with zero pit stones
        List<Pit> result = game.getPits()
                .stream()
                .filter(playerPits)
                .collect(Collectors.toList());
        // comparing the result size with the number the pits in front of each player
        return result.size() == 6;

    }

}