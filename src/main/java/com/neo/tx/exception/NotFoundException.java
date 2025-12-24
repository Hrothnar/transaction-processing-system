package com.neo.tx.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends ApiException {

    public NotFoundException(String entity, String id) {
        super(
                HttpStatus.NOT_FOUND,
                entity.toLowerCase() + "_not_found",
                entity + " not found",
                entity + " with id [" + id + "] was not found",
                Map.of()
        );
    }
}
