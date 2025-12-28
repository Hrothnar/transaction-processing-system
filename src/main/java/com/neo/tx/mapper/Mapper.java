package com.neo.tx.mapper;

import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static Transaction toEntity(TransactionRequestDto dto) {
        Transaction entity = new Transaction();
        entity.setAmount(dto.amount);
        entity.setCardFingerprint(dto.cardFingerprint);
        entity.setChannel(dto.channel);
        entity.setCountry(dto.country);
        entity.setCurrency(dto.currency);
        entity.setDeviceId(dto.deviceId);
        entity.setExternalId(dto.externalId);
        entity.setInitialized(dto.initialized);
        entity.setIpAddress(dto.ipAddress);
        entity.setMerchantCategory(dto.merchantCategory);
        entity.setMerchantId(dto.merchantId);
        entity.setUserId(dto.userId);
        return entity;
    }

    public static Validation toEntity(ValidationResponseDto dto, Transaction transaction) {
        Validation entity = new Validation();
        entity.setTransaction(transaction);
        entity.setDecision(dto.decision);
        entity.setScore(dto.score);
        entity.setRiskLevel(dto.riskLevel);
        entity.setTriggeredValidators(dto.validationResults);
        return entity;
    }

    public static ValidationResponseDto toDto(Validation entity, long transactionId) {
        ValidationResponseDto dto = new ValidationResponseDto();
        dto.id = entity.getId();
        dto.transactionId = transactionId;
        dto.score = entity.getScore();
        dto.riskLevel = entity.getRiskLevel();
        dto.decision = entity.getDecision();
        dto.validationResults = entity.getTriggeredValidators();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        return dto;
    }

    public static ValidationResponseDto toDto(Validation entity) {
        ValidationResponseDto dto = new ValidationResponseDto();
        dto.id = entity.getId();
        dto.transactionId = entity.getTransaction().getId();
        dto.score = entity.getScore();
        dto.riskLevel = entity.getRiskLevel();
        dto.decision = entity.getDecision();
        dto.validationResults = entity.getTriggeredValidators();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        return dto;
    }
}