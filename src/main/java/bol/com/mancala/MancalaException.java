package bol.com.mancala;

public class MancalaException extends RuntimeException{
    public MancalaException(String message) {
        super(message);
    }

    public MancalaException(String message, Throwable cause) {
        super(message, cause);
    }
}
