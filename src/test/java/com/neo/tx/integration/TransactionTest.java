package com.neo.tx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.model.OutboxEvent;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.TransactionJpaRepository;
import com.neo.tx.repository.TransactionRepository;
import com.neo.tx.repository.ValidationJpaRepository;
import com.neo.tx.repository.ValidationRepository;
import com.neo.tx.repository.specification.EventOutboxJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest extends IntegrationTestConfig {

    @Autowired
    Utility utility;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @Autowired
    TransactionJpaRepository transactionJpaRepository;

    @Autowired
    ValidationJpaRepository validationJpaRepository;

    @Autowired
    EventOutboxJpaRepository eventOutboxJpaRepository;

    @MockitoSpyBean
    ValidationRepository validationRepository;

    @MockitoSpyBean
    TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void shouldValidateTransaction() throws Exception {
        String transactionInput = """
                {
                  "externalId": 1,
                  "userId": "c1c3a7c0-1c3b-4a2b-9e5a-7b3c1b2a9f11",
                  "merchantId": "9a1d6f88-0f93-4e58-9dd7-1c9d4a7d6c22",
                  "deviceId": "android-13-pixel-7",
                  "amount": 7150.50,
                  "currency": "USD",
                  "initialized": 1735689600,
                  "merchantCategory": "GROCERIES",
                  "channel": "WEB",
                  "ipAddress": "192.168.1.10",
                  "country": "AGO",
                  "cardFingerprint": "fp_9d82kdk29d"
                }
                """;

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        ArgumentCaptor<Validation> validationCaptor = ArgumentCaptor.forClass(Validation.class);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/v1/transaction/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionInput))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.decision").exists())
                .andReturn();

        Mockito.verify(transactionRepository).save(transactionCaptor.capture());
        Mockito.verify(validationRepository).save(validationCaptor.capture());

        TransactionRequestDto transactionRequestDto = om.readValue(transactionInput, TransactionRequestDto.class);
        ValidationResponseDto validationResponseDto = om.readValue(mvcResult.getResponse().getContentAsString(), ValidationResponseDto.class);
        Validation savedValidation = validationCaptor.getValue();
        Transaction savedTransaction = transactionCaptor.getValue();
        List<Validation> validations = validationJpaRepository.findAll();
        List<Transaction> transactions = transactionJpaRepository.findAll();
        Validation dbValidation = validations.get(0);
        Transaction dbTransaction = transactions.get(0);
        String[] expectedCodes = new String[]{"HIGH_AMOUNT", "HIGH_RISK_COUNTRY"};
        Object[] gotValidationCodes = validationResponseDto.validationResults.stream().map((e) -> e.code).toArray();
        Object[] dbValidationCodes = dbValidation.getTriggeredValidators().stream().map((e) -> e.code).toArray();

        assertEquals(1, validations.size());
        assertEquals(1, transactions.size());

        assertEquals(dbValidation.getDecision(), savedValidation.getDecision());
        assertEquals(dbValidation.getDecision(), validationResponseDto.decision);

        assertEquals(dbValidation.getRiskLevel(), savedValidation.getRiskLevel());
        assertEquals(dbValidation.getRiskLevel(), validationResponseDto.riskLevel);

        assertEquals(dbValidation.getScore(), savedValidation.getScore());
        assertEquals(dbValidation.getScore(), validationResponseDto.score);

        assertEquals(transactionRequestDto.externalId, savedTransaction.getExternalId());
        assertEquals(transactionRequestDto.externalId, dbTransaction.getExternalId());

        assertEquals(validationResponseDto.transactionId, dbTransaction.getId());
        assertEquals(validationResponseDto.transactionId, dbValidation.getTransaction().getId());

        assertEquals(transactionRequestDto.cardFingerprint, savedTransaction.getCardFingerprint());
        assertEquals(transactionRequestDto.merchantCategory, savedTransaction.getMerchantCategory());
        assertEquals(transactionRequestDto.cardFingerprint, dbTransaction.getCardFingerprint());
        assertEquals(transactionRequestDto.merchantCategory, dbTransaction.getMerchantCategory());

        assertArrayEquals(expectedCodes, gotValidationCodes);
        assertArrayEquals(expectedCodes, dbValidationCodes);

        Mockito.verify(validationRepository, Mockito.times(1)).save(Mockito.any(Validation.class));
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    public void shouldThrowAnErrorBecauseOfInvalidInput() throws Exception {
        String transactionInput = """
                {
                  "externalId": 1,
                  "userId": "c1c3a7c0-1c3b-4a2b-9e5a-7b3c1b2a9f11",
                  "merchantId": "9a1d6f88-0f93-4e58-9dd7-1c9d4a7d6c22",
                  "deviceId": "android-13-pixel-7",
                  "amount": MANY,
                  "currency": "BUCKS",
                  "initialized": 1735689600,
                  "merchantCategory": "GROCERIES",
                  "channel": "WEB",
                  "ipAddress": "192.168.1.10",
                  "country": "CALIFORNIA",
                  "cardFingerprint": "fp_9d82kdk29d"
                }
                """;

        mvc.perform(MockMvcRequestBuilders.post("/v1/transaction/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionInput))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void shouldInsertEventInOutboxOnTransactionValidation() throws Exception {
        utility.insertTransaction();

        List<OutboxEvent> events = eventOutboxJpaRepository.findAll();
        OutboxEvent outboxEvent = events.get(0);

        assertEquals(1, events.size());

        System.out.println(outboxEvent);
        Validation validation = om.readValue(outboxEvent.getPayload(), Validation.class);

        assertEquals(Decision.REVIEW, validation.getDecision());
        assertEquals(RiskLevel.MEDIUM, validation.getRiskLevel());
    }
}