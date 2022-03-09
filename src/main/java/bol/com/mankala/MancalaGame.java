package bol.com.mankala;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static bol.com.mankala.MancalaConstants.*;

public class MancalaGame implements Serializable {

    private String id;

    private List<MancalaPit> pits;

    private Players playerTurn;

    private int currentPitIndex;

    public MancalaGame() {
        this(DEFAULT_PIT_STONES);
    }

    public MancalaGame(int pitStones) {
        this.pits = Arrays.asList(
                new MancalaPit(FIRST_PIT_PLAYER_A, pitStones),
                new MancalaPit(SECOND_PIT_PLAYER_A, pitStones),
                new MancalaPit(THIRD_PIT_PLAYER_A, pitStones),
                new MancalaPit(FORTH_PIT_PLAYER_A, pitStones),
                new MancalaPit(FIFTH_PIT_PLAYER_A, pitStones),
                new MancalaPit(SIXTH_PIT_PLAYER_A, pitStones),
                new MancalaBigPit(RIGHT_BIG_PIT_ID),
                new MancalaPit(FIRST_PIT_PLAYER_B, pitStones),
                new MancalaPit(SECOND_PIT_PLAYER_B, pitStones),
                new MancalaPit(THIRD_PIT_PLAYER_B, pitStones),
                new MancalaPit(FORTH_PIT_PLAYER_B, pitStones),
                new MancalaPit(FIFTH_PIT_PLAYER_B, pitStones),
                new MancalaPit(SIXTH_PIT_PLAYER_B, pitStones),
                new MancalaBigPit(LEFT_BIG_PIT_ID));
    }


    public MancalaGame(String id, Integer pitStones) {
        this (pitStones);
        this.id = id;
    }

    // returns the corresponding pit of particular index
    public MancalaPit getPit(Integer pitIndex) throws MancalaException {
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
