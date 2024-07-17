package com.ziblu.springit.domain.validator;

import com.ziblu.springit.domain.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, User> {

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {

    }
}