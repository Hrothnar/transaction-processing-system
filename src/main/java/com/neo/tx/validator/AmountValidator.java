package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AmountValidator implements Evaluate {

    private final Properties properties;

    public AmountValidator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        if (transaction.amount.compareTo(BigDecimal.valueOf(9999)) == 1) { // TODO could include currency converter
            return List.of(new ValidationResultDto("VERY_HIGH_AMOUNT", "The transaction is above 9999 USD", 60));
        }

        if (transaction.amount.compareTo(BigDecimal.valueOf(4999)) == 1) {
            return List.of(new ValidationResultDto("HIGH_AMOUNT", "The transaction is above 4999 USD", 40));
        }

        return List.of();
    }
}