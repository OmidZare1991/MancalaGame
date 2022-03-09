package bol.com.mancala;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static bol.com.mancala.MancalaConstants.*;

public class MancalaGame implements Serializable {

    private String id;

    private List<Pit> pits;

    private Players playerTurn;

    private int currentPitIndex;

    public MancalaGame() {
        this(DEFAULT_PIT_STONES);
    }

    public MancalaGame(int pitStones) {
        this.pits = Arrays.asList(
                new Pit(FIRST_PIT_PLAYER_A, pitStones),
                new Pit(SECOND_PIT_PLAYER_A, pitStones),
                new Pit(THIRD_PIT_PLAYER_A, pitStones),
                new Pit(FORTH_PIT_PLAYER_A, pitStones),
                new Pit(FIFTH_PIT_PLAYER_A, pitStones),
                new Pit(SIXTH_PIT_PLAYER_A, pitStones),
                new BigPit(RIGHT_BIG_PIT_ID),
                new Pit(FIRST_PIT_PLAYER_B, pitStones),
                new Pit(SECOND_PIT_PLAYER_B, pitStones),
                new Pit(THIRD_PIT_PLAYER_B, pitStones),
                new Pit(FORTH_PIT_PLAYER_B, pitStones),
                new Pit(FIFTH_PIT_PLAYER_B, pitStones),
                new Pit(SIXTH_PIT_PLAYER_B, pitStones),
                new BigPit(LEFT_BIG_PIT_ID));
    }


    public MancalaGame(String id, Integer pitStones) {
        this (pitStones);
        this.id = id;
    }

    // returns the corresponding pit of particular index
    public Pit getPit(Integer pitIndex) throws MancalaException {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw  new MancalaException("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

    public Players getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Players playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }
}
