package com.neo.tx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.tx.model.OutboxEvent;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.EventOutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventOutboxService {

    private final ObjectMapper objectMapper;
    private final EventOutboxRepository eventOutboxRepository;

    public EventOutboxService(ObjectMapper objectMapper, EventOutboxRepository eventOutboxRepository) {
        this.objectMapper = objectMapper;
        this.eventOutboxRepository = eventOutboxRepository;
    }

    @Transactional
    public void writeValidationCompleted(Transaction transaction, Validation validation, String pipeline) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("transactionId", transaction.getId());
        payload.put("externalId", transaction.getExternalId());
        payload.put("userId", transaction.getUserId());
        payload.put("pipeline", pipeline);
        payload.put("score", validation.getScore());
        payload.put("riskLevel", validation.getRiskLevel().name());
        payload.put("decision", validation.getDecision().name());
        payload.put("triggeredValidators", validation.getTriggeredValidators());
        payload.put("occurredAt", Instant.now().toString());

        String json;

        try {
            json = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize outbox payload", ex);
        }

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateType("Transaction");
        outboxEvent.setAggregateId(String.valueOf(transaction.getId()));
        outboxEvent.setEventType("TransactionValidated");
        outboxEvent.setTopic("transaction.validation.completed");
        outboxEvent.setEventKey(String.valueOf(transaction.getExternalId()));
        outboxEvent.setPayload(json);
        outboxEvent.setStatus(OutboxEvent.Status.NEW);
        outboxEvent.setAttempts(0);
        outboxEvent.setAvailableAt(Instant.now());

        eventOutboxRepository.save(outboxEvent);
    }
}