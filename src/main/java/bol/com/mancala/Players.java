package bol.com.mancala;

public enum Players {
    PLAYER_A("A"), PLAYER_B("B");

    private String turn;

    Players(String turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return turn;
    }

}
