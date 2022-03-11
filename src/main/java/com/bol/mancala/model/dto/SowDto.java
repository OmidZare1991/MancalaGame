package com.bol.mancala.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SowDto {
    @NotEmpty(message = "gameId cannot be empty or null")
    private String gameId;
    @NotNull(message = "pitId cannot be null")
    private Integer pitId;

    public SowDto(String gameId, Integer pitId) {
        this.gameId = gameId;
        this.pitId = pitId;
    }

    public SowDto() {
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getPitId() {
        return pitId;
    }

    public void setPitId(Integer pitId) {
        this.pitId = pitId;
    }

    @Override
    public String toString() {
        return "{" +
                "gameId='" + gameId + '\'' +
                ", pitId=" + pitId +
                '}';
    }
}
