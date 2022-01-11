package org.dice.alk.exception;

public class InputProcessorException extends RuntimeException {
    public InputProcessorException(String message) {
        super(message);
    }

    public InputProcessorException(String message, Exception ex) {
        super(message, ex);
    }
}
