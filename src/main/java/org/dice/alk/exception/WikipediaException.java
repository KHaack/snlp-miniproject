package org.dice.alk.exception;

public class WikipediaException extends RuntimeException {
    public WikipediaException(String message) {
        super(message);
    }

    public WikipediaException(String message, Exception ex) {
        super(message, ex);
    }
}
