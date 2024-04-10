package de.mfberg.bbak.exceptions;

public class UserConflictException extends RuntimeException {
    public UserConflictException(String message) {
        super(message);
    }
}
