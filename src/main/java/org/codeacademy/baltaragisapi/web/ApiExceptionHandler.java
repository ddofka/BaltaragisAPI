package org.codeacademy.baltaragisapi.web;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.codeacademy.baltaragisapi.exception.ConflictException;
import org.codeacademy.baltaragisapi.exception.InsufficientStockException;
import org.codeacademy.baltaragisapi.exception.NotFoundException;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    private record ProblemDetails(String type, String title, int status, String detail, String instance,
                                  String code, String timestamp, List<Map<String, String>> errors) {}

    private ResponseEntity<ProblemDetails> problem(HttpStatus status, String code, String detail, WebRequest req, List<Map<String, String>> errors) {
        String path = req.getDescription(false).replace("uri=", "");
        ProblemDetails body = new ProblemDetails(
                URI.create("https://api.baltaragis.dev/problems/" + code.toLowerCase()).toString(),
                status.getReasonPhrase(),
                status.value(),
                detail,
                path,
                code,
                OffsetDateTime.now().toString(),
                errors
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(NotFoundException ex, WebRequest req) {
        return problem(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), req, null);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ProblemDetails> handleStock(InsufficientStockException ex, WebRequest req) {
        return problem(HttpStatus.CONFLICT, "INSUFFICIENT_STOCK", ex.getMessage(), req, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetails> handleConflict(ConflictException ex, WebRequest req) {
        String code = ex.getMessage().contains("slug") ? "DUPLICATE_SLUG" : "CONFLICT";
        return problem(HttpStatus.CONFLICT, code, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetails> handleValidation(ValidationException ex, WebRequest req) {
        List<Map<String, String>> errs = ex.getFieldErrors() == null ? null : ex.getFieldErrors().entrySet()
                .stream().map(e -> Map.of("field", e.getKey(), "message", e.getValue())).toList();
        return problem(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", ex.getMessage(), req, errs);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleAuthCredentialsNotFound(AuthenticationCredentialsNotFoundException ex, WebRequest req) {
        ProblemDetails problem = new ProblemDetails(
            "about:blank",
            "Unauthorized",
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication required or token missing/invalid.",
            req.getDescription(false).replace("uri=", ""),
            "UNAUTHORIZED",
            java.time.OffsetDateTime.now().toString(),
            null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleAccessDenied(AccessDeniedException ex, WebRequest req) {
        ProblemDetails problem = new ProblemDetails(
            "about:blank",
            "Forbidden",
            HttpStatus.FORBIDDEN.value(),
            "You do not have permission to access this resource.",
            req.getDescription(false).replace("uri=", ""),
            "FORBIDDEN",
            java.time.OffsetDateTime.now().toString(),
            null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }
}


