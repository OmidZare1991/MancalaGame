package bol.com.mancala;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SowApiRequestModel {
    @NotEmpty(message = "gameId cannot be empty or null")
    private String gameId;
    @NotNull(message = "pitIndex cannot be null")
    private Integer pitIndex;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getPitIndex() {
        return pitIndex;
    }

    public void setPitIndex(Integer pitIndex) {
        this.pitIndex = pitIndex;
    }

    @Override
    public String toString() {
        return "{" +
                "gameId='" + gameId + '\'' +
                ", pitIndex=" + pitIndex +
                '}';
    }
}
