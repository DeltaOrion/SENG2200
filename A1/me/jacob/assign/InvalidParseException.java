package me.jacob.assign;

/**
 * File: InvalidParseException.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * A simple checked exception which is thrown when a {@link Parseable} is unable to parse its input for any reason
 */
public class InvalidParseException extends Exception {

    /**
     * Documented in {@link Exception}
     */
    public InvalidParseException(String message) {
        super(message);
    }

    /**
     * Documented in {@link Exception}
     */
    public InvalidParseException(String message, Throwable e) {
        super(message,e);
    }
}
