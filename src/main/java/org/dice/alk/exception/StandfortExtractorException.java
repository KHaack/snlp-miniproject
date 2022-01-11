package org.dice.alk.exception;

public class StandfortExtractorException extends RuntimeException {
    public StandfortExtractorException(String message) {
        super(message);
    }

    public StandfortExtractorException(String message, Exception ex) {
        super(message, ex);
    }
}
