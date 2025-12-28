package com.neo.tx.validator;

import com.neo.tx.config.Properties;
import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class NightTimeValidator implements Evaluate {

    private final Properties properties;

    public NightTimeValidator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public List<ValidationResultDto> evaluate(TransactionRequestDto transaction) {
        int hourByUTC = Instant.ofEpochSecond(transaction.initialized).atZone(ZoneOffset.UTC).getHour();

        if (hourByUTC >= 2 && hourByUTC < 5) {
            return List.of(new ValidationResultDto("NIGH_TIME", "The transaction is made between 02:00 AM and 05:00 AM", 15));
        }

        return List.of();
    }
}
