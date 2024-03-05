package ru.wwerlosh.task.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class GreaterOrEqualsToValidator implements ConstraintValidator<GreaterOrEqualsTo, BigDecimal> {
    private BigDecimal minValue;
    private String message;

    @Override
    public void initialize(GreaterOrEqualsTo annotation) {
        this.minValue = new BigDecimal(annotation.value());
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.compareTo(minValue) < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

