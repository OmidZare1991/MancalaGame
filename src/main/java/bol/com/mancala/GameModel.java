package bol.com.mancala;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static bol.com.mancala.MancalaConstants.*;

public class GameModel implements Serializable {

    private String gameId;
    private List<Pit> pits;
    private Players playerTurn;
    private int currentPitIndex;

    public GameModel() {
        this(DEFAULT_PIT_STONES);
    }

    public GameModel(int pitStones) {
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


    public GameModel(String gameId, Integer pitStones) {
        this(pitStones);
        this.gameId = gameId;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
        GameModel that = (GameModel) o;
        return currentPitIndex == that.currentPitIndex &&
                Objects.equals(gameId, that.gameId) &&
                Objects.equals(pits, that.pits) &&
                playerTurn == that.playerTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, pits, playerTurn, currentPitIndex);
    }

    @Override
    public String toString() {
        return "{" +
                "gameId='" + gameId + '\'' +
                ", pits=" + pits +
                ", playerTurn=" + playerTurn +
                ", currentPitIndex=" + currentPitIndex +
                '}';
    }
}
