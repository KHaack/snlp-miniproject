package org.dice.alk.exception;

public class WordNetException extends RuntimeException {
    public WordNetException(String message, Exception ex) {
        super(message, ex);
    }
}
