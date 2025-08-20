package org.codeacademy.baltaragisapi.web;

import org.codeacademy.baltaragisapi.dto.ErrorResponse;
import org.codeacademy.baltaragisapi.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        String code = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("stock")
                ? "INSUFFICIENT_STOCK" : "DOMAIN_ERROR";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().code(code).message(ex.getMessage()).build());
    }
}


