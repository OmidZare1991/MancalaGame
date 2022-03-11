package com.bol.mancala.model.dto;

public class GameDto {
    private String gameId;

    public GameDto() {
    }

    public GameDto(String gameInstance) {
        this.gameId = gameInstance;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "{" +
                "gameId='" + gameId + '\'' +
                '}';
    }
}
