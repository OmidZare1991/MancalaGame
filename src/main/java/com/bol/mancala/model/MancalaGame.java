package com.bol.mancala.model;

import com.bol.mancala.common.Players;
import com.bol.mancala.exception.MancalaException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MancalaGame implements Serializable {

    private String id;
    private List<Pit> pits;
    private Players playerTurn;
    private int currentPitIndex;

    public MancalaGame(String id, List<Pit> pits) {
        this.id = id;
        this.pits = pits;
    }

    // returns the corresponding pit of particular index
    public Pit getPit(Integer pitIndex) throws MancalaException {
        try {
            return this.pits.get(pitIndex - 1);
        } catch (Exception e) {
            throw new MancalaException("Invalid pitIndex:" + pitIndex + " has given!");
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

    public int getCurrentPitIndex() {
        return currentPitIndex;
    }

    public void setCurrentPitIndex(int currentPitIndex) {
        this.currentPitIndex = currentPitIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MancalaGame that = (MancalaGame) o;
        return currentPitIndex == that.currentPitIndex &&
                Objects.equals(id, that.id) &&
                Objects.equals(pits, that.pits) &&
                playerTurn == that.playerTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pits, playerTurn, currentPitIndex);
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", pits=" + pits +
                ", playerTurn=" + playerTurn +
                ", currentPitIndex=" + currentPitIndex +
                '}';
    }
}