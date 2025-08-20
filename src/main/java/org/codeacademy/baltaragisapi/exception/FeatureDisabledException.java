package org.codeacademy.baltaragisapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class FeatureDisabledException extends RuntimeException {
    
    public FeatureDisabledException(String message) {
        super(message);
    }
    
    public FeatureDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
