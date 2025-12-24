package com.neo.tx.service;

import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.mapper.Mapper;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final PipelineService pipelineService;
    private final TransactionRepository transactionRepository;
    private final ValidationService validationService;

    public TransactionService(PipelineService pipelineService, TransactionRepository transactionRepository, ValidationService validationService) {
        this.pipelineService = pipelineService;
        this.transactionRepository = transactionRepository;
        this.validationService = validationService;
    }

    @Transactional
    public ValidationResponseDto validateTransaction(TransactionRequestDto transactionRequestDto, String pipeline) { // TODO include multiple pipeline support
        List<ValidationResultDto> results = pipelineService.getDefaultChain().evaluate(transactionRequestDto);
        ValidationResponseDto analyzedResults = validationService.analyzeValidationResponses(results);
        Transaction transaction = transactionRepository.save(transactionRequestDto);
        Validation validation = validationService.save(analyzedResults, transaction);
        return Mapper.toDto(validation, transaction.getId());
    }
}