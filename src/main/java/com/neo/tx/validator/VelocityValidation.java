package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.model.Validation;
import com.neo.tx.repository.ValidationRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class VelocityValidation implements Evaluate {

    private final Properties properties;
    private final ValidationRepository validationRepository;

    public VelocityValidation(Properties properties, ValidationRepository validationRepository) {
        this.properties = properties;
        this.validationRepository = validationRepository;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        int minutes = 5;
        int threshold = 3;
        Instant now = Instant.now();

        Page<Validation> pagedRecords = validationRepository.find(transaction.userId, null, null, now.minus(minutes, ChronoUnit.MINUTES), now, 0, 100);

        if (pagedRecords.getContent().size() > threshold) {
            return List.of(new ValidationResultDto("HIGH_VELOCITY", "Transactions under user id " + transaction.userId + "are too frequent. Threshold: " + threshold, 25));
        }

        return List.of();
    }
}