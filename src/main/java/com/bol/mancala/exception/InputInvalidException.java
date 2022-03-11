package com.bol.mancala.exception;

public class InputInvalidException extends RuntimeException {
    public InputInvalidException(String message) {
        super(message);
    }

    public InputInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
