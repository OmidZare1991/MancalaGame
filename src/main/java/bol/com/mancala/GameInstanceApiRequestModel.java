package bol.com.mancala;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameInstanceApiRequestModel {
    @JsonProperty("gameId")
    private String gameId;

    public GameInstanceApiRequestModel() {
    }

    public GameInstanceApiRequestModel(String gameInstance) {
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
