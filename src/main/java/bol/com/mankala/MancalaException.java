package bol.com.mankala;

public class MancalaException extends RuntimeException{
    public MancalaException(String message) {
        super(message);
    }

    public MancalaException(String message, Throwable cause) {
        super(message, cause);
    }
}
