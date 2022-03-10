package bol.com.mancala;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(value = InputInvalidException.class)
    public ResponseEntity<String> getException(InputInvalidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
