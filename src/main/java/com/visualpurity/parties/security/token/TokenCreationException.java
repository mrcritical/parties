package com.visualpurity.parties.security.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TokenCreationException extends RuntimeException {
    public TokenCreationException() {
    }

    public TokenCreationException(String message) {
        super(message);
    }

    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
