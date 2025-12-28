package com.neo.tx.service;

import com.neo.tx.validator.*;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    private final AmountValidator amountValidator;
    private final CountryMismatchValidation countryMismatchValidation;
    private final HighRiskCountryValidator highRiskCountryValidator;
    private final HighRiskMerchantValidator highRiskMerchantValidator;
    private final NightTimeValidator nightTimeValidator;
    private final VelocityValidation velocityValidation;

    public PipelineService(
            AmountValidator amountValidator,
            CountryMismatchValidation countryMismatchValidation,
            HighRiskCountryValidator highRiskCountryValidator,
            HighRiskMerchantValidator highRiskMerchantValidator,
            NightTimeValidator nightTimeValidator,
            VelocityValidation velocityValidation
    ) {
        this.amountValidator = amountValidator;
        this.countryMismatchValidation = countryMismatchValidation;
        this.highRiskCountryValidator = highRiskCountryValidator;
        this.highRiskMerchantValidator = highRiskMerchantValidator;
        this.nightTimeValidator = nightTimeValidator;
        this.velocityValidation = velocityValidation;
    }

    public Evaluate getDefaultChain() {
        return amountValidator
                .and(countryMismatchValidation)
                .and(highRiskCountryValidator)
                .and(highRiskMerchantValidator)
                .and(nightTimeValidator)
                .and(velocityValidation);
    }
}