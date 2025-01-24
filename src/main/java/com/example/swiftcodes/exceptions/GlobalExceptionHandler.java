package com.example.swiftcodes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SwiftCodeNotFoundException extends RuntimeException {
        public SwiftCodeNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class SwiftCodeAlreadyExistsException extends RuntimeException {
        public SwiftCodeAlreadyExistsException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(SwiftCodeAlreadyExistsException.class)
    public ResponseEntity<Object> handleSwiftCodeAlreadyExists(SwiftCodeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<Object> handleSwiftCodeNotFound(SwiftCodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", ex.getMessage()
        ));
    }
}
