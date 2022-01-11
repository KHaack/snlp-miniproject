package org.dice.alk.exception;

public class NerException extends RuntimeException {
    public NerException(String message) {
        super(message);
    }

    public NerException(String message, Exception ex) {
        super(message, ex);
    }
}
