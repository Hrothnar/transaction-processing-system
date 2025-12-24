package com.neo.tx.controller;

import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/v1/validation")
@Tag(name = "Validation", description = "Endpoints for interacting with 'Validations'")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Operation(summary = "Get a transaction validation result by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Result found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Result not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ValidationResponseDto> getValidation(
            @Parameter(description = "Validation id", example = "843")
            @PathVariable("id") long id
    ) {
        ValidationResponseDto validationResponseDto = validationService.find(id);
        return ResponseEntity.ok(validationResponseDto);
    }

    @Operation(summary = "Search transaction validation results", description = "Returns paginated validation results filtered by optional criteria. All parameters are optional. If no filters are provided, all results are returned")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Results found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // TODO make a dedicated page view?
            @ApiResponse(responseCode = "400", description = "Invalid query parameters", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Page<ValidationResponseDto>> find(
            @Parameter(description = "Filter by user ID", example = "c1c3a7c0-1c3b-4a2b-9e5a-7b3c1b2a9f11") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Filter by risk level", schema = @Schema(implementation = RiskLevel.class)) @RequestParam(required = false) RiskLevel riskLevel,
            @Parameter(description = "Filter by final decision", schema = @Schema(implementation = Decision.class)) @RequestParam(required = false) Decision decision,
            @Parameter(description = "Transaction initialization from time (inclusive)", example = "2025-01-01T00:00:00Z") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @Parameter(description = "Transaction initialization to time (inclusive)", example = "2025-12-31T23:59:59Z") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @Parameter(description = "Page number (starting from 0)", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size (max 200)", example = "20") @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size
    ) {
        Page<ValidationResponseDto> result = validationService.find(userId, riskLevel, decision, from, to, page, size);
        return ResponseEntity.ok(result);
    }
}