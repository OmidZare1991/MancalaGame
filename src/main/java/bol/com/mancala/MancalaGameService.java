package bol.com.mancala;

public interface MancalaGameService<INPUT1, INPUT2> {
    GameModel sow(INPUT1 input1, INPUT2 input2);
}
