package com.neo.tx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/v1/utility")
@Tag(name = "Utility", description = "Utility endpoints, for health, metrics, etc")
public class UtilityController {

    @Operation(summary = "Liveness check")
    @ApiResponse(responseCode = "200", description = "Whether the service is up")
    @GetMapping("/health/liveness")
    public ResponseEntity<String> probLiveness() {
        return ResponseEntity.ok("{\"isItCool\":\"true\"}");
    }

    @Operation(summary = "Readiness check")
    @ApiResponse(responseCode = "200", description = "Whether the service is up and ready for processing requests")
    @GetMapping("/health/readiness")
    public ResponseEntity<String> probReadiness() {
        return ResponseEntity.ok("{\"isItCool\":\"true\"}");
    }
}