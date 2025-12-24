package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import com.neo.tx.enums.RiskMerchant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HighRiskMerchantValidator implements Evaluate {

    private final Properties properties;

    public HighRiskMerchantValidator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        if (RiskMerchant.contains(transaction.merchantCategory)) {
            return List.of(new ValidationResultDto("HIGH_RISK_MERCHANT", "The transaction is made in one of the risk merchant categories", 35));
        }

        return List.of();
    }
}
