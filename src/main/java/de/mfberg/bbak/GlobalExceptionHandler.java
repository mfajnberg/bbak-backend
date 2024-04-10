package de.mfberg.bbak;

import de.mfberg.bbak.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ex.getMessage();
    }

    // used in AssetService.loadResource()
    @ExceptionHandler(ResourceLoadingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceLoadingException(ResourceLoadingException ex) {
        // Log the exception, return a custom error message, etc.
        return "Error loading resource: " + ex.getMessage();
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDataException(InvalidDataException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserConflictException(UserConflictException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(JobConflictException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleJobConflictException(JobConflictException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(JobSchedulingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleJobSchedulingException(JobSchedulingException ex) {
        return "Error scheduling Job: " + ex.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ex.getMessage();
    }
}
