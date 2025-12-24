package com.neo.tx.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> handleApiException(ApiException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(exception.getStatus());
        pd.setTitle(exception.getTitle());
        pd.setDetail(exception.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", exception.getCode());
        pd.setProperty("meta", exception.getMeta());
        return ResponseEntity.status(exception.getStatus()).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        List<Map<String, Object>> errors = exception.getBindingResult().getFieldErrors().stream().map(this::fieldErrorToMap).toList();
        pd.setTitle("Validation failed");
        pd.setDetail("Request body has invalid fields");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Malformed JSON");
        pd.setDetail(Optional.ofNullable(exception.getMostSpecificCause()).map(Throwable::getMessage).orElse(exception.getMessage()));
        pd.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException exception, HttpServletRequest request) {
        String constraint = extractConstraintName(exception);
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Data integrity violation");
        pd.setDetail("Request conflicts with existing data");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "db.constraint_violation");

        if (constraint != null) {
            pd.setProperty("constraint", constraint);
        }

        return ResponseEntity.status(status).body(pd);
    }

    private String extractConstraintName(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {

            if (current instanceof ConstraintViolationException exception) {
                return exception.getConstraintName();
            }

            current = current.getCause();
        }

        return null;
    }

    private Map<String, Object> fieldErrorToMap(FieldError error) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("field", error.getField());
        map.put("message", error.getDefaultMessage());
        map.put("rejectedValue", error.getRejectedValue());
        return map;
    }
}