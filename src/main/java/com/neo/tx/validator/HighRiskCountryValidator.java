package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.enums.RiskCountry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HighRiskCountryValidator implements Evaluate {

    private final Properties properties;

    public HighRiskCountryValidator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        if (RiskCountry.contains(transaction.country)) {
            return List.of(new ValidationResultDto("HIGH_RISK_COUNTRY", "The transaction was made in high-risk country " + transaction.country, 30));
        }

        return List.of();
    }
}
