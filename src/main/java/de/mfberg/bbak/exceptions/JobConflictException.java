package de.mfberg.bbak.exceptions;

public class JobConflictException extends RuntimeException {
    public JobConflictException(String message) {
        super(message);
    }
}