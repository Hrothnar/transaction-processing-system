package com.neo.tx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class Utility {

    private static final AtomicLong EXTERNAL_ID = new AtomicLong(1);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper om;

    public ValidationResponseDto insertTransaction() throws Exception {
        String formatted = String.format("""
                {
                  "externalId": %d,
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
                """, EXTERNAL_ID.getAndIncrement());

        TransactionRequestDto transactionRequestDto = om.readValue(formatted, TransactionRequestDto.class);

        return transactionService.validateTransaction(transactionRequestDto, "default");
    }
}