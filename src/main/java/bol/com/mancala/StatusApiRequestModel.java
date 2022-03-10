package bol.com.mancala;

import javax.validation.constraints.NotEmpty;

public class StatusApiRequestModel {
    @NotEmpty(message = "gameId cannot be null oe empty")
    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
