package com.bol.mancala.common;

public enum Players {
    PLAYER_A("PLAYER_A"), PLAYER_B("PLAYER_B");

    private String turn;

    Players(String turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return turn;
    }

}
