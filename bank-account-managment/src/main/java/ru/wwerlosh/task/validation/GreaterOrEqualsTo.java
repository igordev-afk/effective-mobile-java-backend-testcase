package ru.wwerlosh.task.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreaterOrEqualsToValidator.class)
public @interface GreaterOrEqualsTo {
    String value() default "0";
    String message() default "The value must be greater than or equal to the specified number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

