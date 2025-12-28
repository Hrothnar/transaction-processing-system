package com.neo.tx.repository;

import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.mapper.Mapper;
import com.neo.tx.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepository {

    private final TransactionJpaRepository repository;

    public TransactionRepository(TransactionJpaRepository repository) {
        this.repository = repository;
    }

    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    public Transaction save(TransactionRequestDto transactionRequestDto) {
        return save(Mapper.toEntity(transactionRequestDto));
    }

    public List<Transaction> find(UUID userId, Instant from) {
        return repository.findByUserIdAndInitializedGreaterThanEqualOrderByInitializedDesc(userId, from.getEpochSecond());
    }
}