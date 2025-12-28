package com.neo.tx.service;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.exception.NotFoundException;
import com.neo.tx.mapper.Mapper;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.ValidationRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ValidationService {

    private final Properties properties;
    private final ValidationRepository validationRepository;

    public ValidationService(Properties properties, ValidationRepository validationRepository) {
        this.properties = properties;
        this.validationRepository = validationRepository;
    }

    public ValidationResponseDto analyzeValidationResponses(List<ValidationResultDto> results) {
        RiskLevel riskLevel = RiskLevel.HIGH;
        Decision decision = Decision.BLOCK;

        int totalScore = results.stream().mapToInt((result) -> result.scoreDelta).sum();

        if (totalScore <= properties.riskThresholdMedium) {
            riskLevel = RiskLevel.MEDIUM;
            decision = Decision.REVIEW;
        }

        if (totalScore <= properties.riskThresholdLow) {
            riskLevel = RiskLevel.LOW;
            decision = Decision.ALLOW;
        }

        return new ValidationResponseDto(totalScore, riskLevel, decision, results);
    }

    public Validation save(ValidationResponseDto validationResponseDto, Transaction transaction) {
        return validationRepository.save(validationResponseDto, transaction);
    }

    public ValidationResponseDto find(long id) {
        Validation validation = validationRepository.find(id);

        if (validation == null) {
            throw new NotFoundException(Validation.class.getSimpleName(), String.valueOf(id));
        }

        return Mapper.toDto(validation, validation.getTransaction().getId());
    }

    public Page<ValidationResponseDto> find(UUID userId, RiskLevel riskLevel, Decision decision, Instant from, Instant to, int page, int size) {
        Page<Validation> validations = validationRepository.find(userId, riskLevel, decision, from, to, page, size);
        return validations.map(Mapper::toDto);
    }
}