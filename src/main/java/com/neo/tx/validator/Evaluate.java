package com.neo.tx.validator;

import com.neo.tx.dto.TransactionRequestDto;
import com.neo.tx.dto.ValidationResultDto;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface Evaluate {

    List<ValidationResultDto> evaluate(TransactionRequestDto transaction);

    default Evaluate and(Evaluate that) {
        return (transaction) -> {
            List<ValidationResultDto> thisResult = this.evaluate(transaction);
            List<ValidationResultDto> thatResult = that.evaluate(transaction);

            List<ValidationResultDto> validationResults = new ArrayList<>();

            validationResults.addAll(thisResult);
            validationResults.addAll(thatResult);

            return validationResults;
        };
    }
}
