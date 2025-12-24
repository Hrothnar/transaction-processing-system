package com.neo.tx.repository;

import com.neo.tx.dto.ValidationResponseDto;
import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.mapper.Mapper;
import com.neo.tx.model.Transaction;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.specification.ValidationSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class ValidationRepository {

    private final ValidationJpaRepository repository;

    public ValidationRepository(ValidationJpaRepository repository) {
        this.repository = repository;
    }

    public Validation save(Validation validation) {
        return repository.save(validation);
    }

    public Validation save(ValidationResponseDto validationResponseDto, Transaction transaction) {
        return save(Mapper.toEntity(validationResponseDto, transaction));
    }

    public Validation find(long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Validation> find(UUID userId, RiskLevel riskLevel, Decision decision, Instant from, Instant to, int page, int size) {
        Specification<Validation> specification = ValidationSpecification.buildCondition(userId, riskLevel, decision, from, to);
        PageRequest pagination = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        return repository.findAll(specification, pagination);
    }
}
