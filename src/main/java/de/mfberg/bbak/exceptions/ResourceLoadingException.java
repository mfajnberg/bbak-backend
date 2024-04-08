package de.mfberg.bbak.exceptions;

public class ResourceLoadingException extends RuntimeException {
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}