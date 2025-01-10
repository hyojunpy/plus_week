package com.example.demo.exception;

import lombok.Generated;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Generated
public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
