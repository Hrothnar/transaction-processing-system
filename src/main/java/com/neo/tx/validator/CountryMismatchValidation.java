package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.model.Transaction;
import com.neo.tx.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
public class CountryMismatchValidation implements Evaluate {

    private final Properties properties;
    private final TransactionRepository transactionRepository;

    public CountryMismatchValidation(Properties properties, TransactionRepository transactionRepository) {
        this.properties = properties;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        Instant monthAgo = Instant.now().minusSeconds(30 * 24 * 60 * 60);
        List<Transaction> transactions = transactionRepository.find(transaction.userId, monthAgo);

        long totalTransactions = transactions.size();
        long countryMatch = transactions.stream().filter((e) -> Objects.equals(e.getCountry(), transaction.country)).count();

        if (countryMatch < (double) totalTransactions / 1.50) {
            return List.of(new ValidationResultDto(
                    "COUNTRY_MISMATCH",
                    "Current country + " + transaction.country + " appears in transactions less than 50% of the time",
                    20
            ));
        }

        return List.of();
    }
}