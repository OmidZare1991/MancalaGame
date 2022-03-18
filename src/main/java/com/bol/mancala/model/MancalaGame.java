package com.bol.mancala.model;

import com.bol.mancala.common.Players;

import java.util.List;

public class MancalaGame {

    private String id;
    private List<Pit> pits;
    private Players playerTurn;
    private Integer currentPitIndex;
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public MancalaGame(String id, List<Pit> pits) {
        this.id = id;
        this.pits = pits;
    }

    // returns the corresponding pit of particular index
    public Pit getPit(Integer pitIndex) {
        try {
            return this.pits.get(pitIndex - 1);
        } catch (Exception e) {
            throw new IndexOutOfBoundsException("Invalid pitIndex:" + pitIndex + " has given!");
        }
    }

    public List<Pit> getPits() {
        return pits;
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

    public Integer getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", pits=" + pits +
                ", playerTurn=" + playerTurn +
                ", currentPitIndex=" + currentPitIndex +
                ", winner='" + winner + '\'' +
                '}';
    }
}
