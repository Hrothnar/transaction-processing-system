package com.neo.tx.dto;

import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;

import java.time.Instant;
import java.util.List;

public class ValidationResponseDto {
    public long id;
    public long transactionId;
    public int score;
    public RiskLevel riskLevel;
    public Decision decision;
    public List<ValidationResultDto> validationResults;
    public Instant createdAt;
    public Instant updatedAt;

    public ValidationResponseDto() {

    }

    public ValidationResponseDto(int score, RiskLevel riskLevel, Decision decision, List<ValidationResultDto> validationResults) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.decision = decision;
        this.validationResults = validationResults;
    }
}