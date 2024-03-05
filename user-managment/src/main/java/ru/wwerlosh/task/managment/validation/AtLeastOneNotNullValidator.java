package ru.wwerlosh.task.managment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, Object> {

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext constraintValidatorContext) {
        if (t == null) {
            return false;
        }

        try {
            String phone = (String) t.getClass().getMethod("getPhone").invoke(t);
            String email = (String) t.getClass().getMethod("getEmail").invoke(t);

            return phone != null || email != null;
        } catch (Exception e) {
            return false;
        }
    }
}
