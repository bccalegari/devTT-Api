package br.com.devtt.enterprise.infrastructure.configuration.spring.validators.implementations;

import br.com.devtt.enterprise.infrastructure.configuration.spring.validators.abstractions.ListContainsValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Sex implements ConstraintValidator<ListContainsValue, String>{
    private final List<String> sexValues = Arrays.asList("M", "F");

    @Override
    public void initialize(ListContainsValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return sexValues.contains(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}