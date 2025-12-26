package com.neo.tx.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.repository.TransactionJpaRepository;
import com.neo.tx.repository.TransactionRepository;
import com.neo.tx.repository.ValidationJpaRepository;
import com.neo.tx.repository.ValidationRepository;
import com.neo.tx.service.TransactionService;
import com.neo.tx.service.ValidationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValidationTest extends IntegrationTestConfig {

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
    TransactionService transactionService;

    @Autowired
    ValidationService validationService;

    @MockitoSpyBean
    ValidationRepository validationRepository;

    @MockitoSpyBean
    TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void shouldFindAndReturnValidationById() throws Exception {

        System.out.println("transactionJpaRepository.findAll().size()");
        System.out.println(transactionJpaRepository.findAll().size());
        ValidationResponseDto validationResponseDto1 = utility.insertTransaction();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/v1/validation/{id}", validationResponseDto1.id))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ValidationResponseDto validationResponseDto = om.readValue(mvcResult.getResponse().getContentAsString(), ValidationResponseDto.class);

        assertNotNull(validationResponseDto);
    }

    @Test
    @Transactional
    public void shouldThrowNotFoundExceptionAndReturnNotFoundResponse() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/v1/validation/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        String expectedResponse = "{\"type\":\"about:blank\",\"title\":\"Validation not found\",\"status\":404,\"detail\":\"Validation with id [1] was not found\",\"instance\":\"/v1/validation/1\",\"code\":\"validation_not_found\",\"meta\":{}}";
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    public void shouldReturnListOfValidations() throws Exception {
        utility.insertTransaction();
        utility.insertTransaction();
        utility.insertTransaction();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(
                        "/v1/validation?riskLevel={riskLevel}",
                        RiskLevel.MEDIUM
                ))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        JsonNode contentNode = om.readTree(mvcResult.getResponse().getContentAsString()).get("content");
        ValidationResponseDto[] validationResponses = om.treeToValue(contentNode, ValidationResponseDto[].class);

        assertEquals(3, validationResponses.length);

        for (int i = 0; i < validationResponses.length; i++) {
            assertEquals(RiskLevel.MEDIUM, validationResponses[i].riskLevel);
        }
    }
}