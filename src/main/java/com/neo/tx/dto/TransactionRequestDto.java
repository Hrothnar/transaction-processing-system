package com.neo.tx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequestDto {

    @Schema(description = "External transaction identifier", example = "433")
    @NotNull
    public Long externalId;

    @Schema(description = "User identifier", example = "c1c3a7c0-1c3b-4a2b-9e5a-7b3c1b2a9f11")
    @NotNull
    public UUID userId;

    @Schema(description = "Merchant identifier", example = "9a1d6f88-0f93-4e58-9dd7-1c9d4a7d6c22")
    @NotNull
    public UUID merchantId;

    @Schema(description = "Optional device identifier", example = "android-13-pixel-7")
    @Size(max = 64)
    public String deviceId;

    @Schema(description = "Transaction amount", example = "125.50")
    @NotNull
    @Positive
    public BigDecimal amount;

    @Schema(description = "ISO 4217 currency code", example = "USD")
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    public String currency;

    @Schema(description = "Transaction initialization time (Unix timestamp, seconds)", example = "1735689600")
    @NotNull
    public Long initialized;

    @Schema(description = "Merchant category code", example = "GROCERIES")
    @NotBlank
    public String merchantCategory;

    @Schema(description = "Transaction channel", example = "WEB")
    @NotBlank
    public String channel;

    @Schema(description = "Client IPv4 address", example = "192.168.1.10")
    @NotBlank
    @Pattern(regexp = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}")
    public String ipAddress;

    @Schema(description = "ISO 3166-1 alpha-3 country code", example = "USA")
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    public String country;

    @Schema(description = "Optional card fingerprint", example = "fp_9d82kdk29d")
    @Size(max = 64)
    public String cardFingerprint;
}