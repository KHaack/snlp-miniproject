package org.dice.alk.exception;

public class TagMeException extends RuntimeException {
    public TagMeException(String message, Exception ex) {
        super(message, ex);
    }
}
