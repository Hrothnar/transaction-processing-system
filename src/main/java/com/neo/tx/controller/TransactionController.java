package com.neo.tx.controller;

import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/transaction")
@Tag(name = "Transaction", description = "Endpoints for interacting with 'Transactions'")
public class TransactionController {

    private final TransactionService validationService;

    public TransactionController(TransactionService validationService) {
        this.validationService = validationService;
    }

    @Operation(summary = "Validate a transaction", description = "Evaluates a transaction using the configured validation pipeline and returns score, risk level, decision, and triggered validators")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction validated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error (invalid request body)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validateTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Transaction data to be evaluated", content = @Content(schema = @Schema(implementation = TransactionRequestDto.class))) @Valid @RequestBody TransactionRequestDto transaction,
            @Parameter(description = "Optional pipeline name", example = "default") @RequestParam(name = "pipeline", required = false, defaultValue = "default") String pipeline
    ) {
        ValidationResponseDto response = validationService.validateTransaction(transaction, pipeline);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}