package de.eric.bankingapp.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GeneralExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleException(Exception ex){
        log.error("Exception thrown", ex);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseStatusException handleResponseException(ResponseStatusException ex){
        return ex;
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class, CsrfException.class, SessionAuthenticationException.class})
    public Exception handleSecurityExceptions(Exception ex){
        log.error("Security Exception", ex);
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

}
