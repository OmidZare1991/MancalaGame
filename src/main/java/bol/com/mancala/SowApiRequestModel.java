package bol.com.mancala;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SowApiRequestModel {
    @NotEmpty(message = "gameId cannot be empty or null")
    @JsonProperty(value = "gameId")
    private String gameId;
    @NotNull(message = "pitIndex cannot be null")
    @JsonProperty(value = "pitIndex")
    private Integer pitIndex;

    public SowApiRequestModel( String gameId, Integer pitIndex) {
        this.gameId = gameId;
        this.pitIndex = pitIndex;
    }

    public SowApiRequestModel() {
    }

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
