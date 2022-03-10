package bol.com.mancala;

public class InputInvalidException extends RuntimeException {
    public InputInvalidException(String message) {
        super(message);
    }

    public InputInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
