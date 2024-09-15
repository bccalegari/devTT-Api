package br.com.devtt.enterprise.infrastructure.configuration.spring.validators.abstractions;

import br.com.devtt.enterprise.infrastructure.configuration.spring.validators.implementations.Sex;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Sex.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ListContainsValue {
    String message() default "Valor inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}