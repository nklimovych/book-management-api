package com.book.management.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearRangeValidator.class)
public @interface ValidYearRange {
    String message() default "{ValidYearRange.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
