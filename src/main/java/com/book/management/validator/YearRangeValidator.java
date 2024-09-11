package com.book.management.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearRangeValidator implements ConstraintValidator<ValidYearRange, Integer> {

    @Override
    public void initialize(ValidYearRange validYearRange) {
        ConstraintValidator.super.initialize(validYearRange);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 0 && value <= Year.now().getValue();
    }
}
