package com.neo.tx.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String title;
    private final Map<String, Object> meta;

    public ApiException(HttpStatus status, String code, String title, String message, Map<String, Object> meta) {
        super(message);
        this.status = status;
        this.code = code;
        this.title = title;
        this.meta = meta;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }
}