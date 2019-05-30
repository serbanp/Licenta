package com.mvrcm.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InsideResourceNotFoundException extends RuntimeException {
    public InsideResourceNotFoundException(String message) {
        super(message);
    }
}
