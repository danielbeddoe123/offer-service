package com.beddoed.offers.web;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Currency;

import static org.apache.commons.lang3.StringUtils.*;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !isBlank(value) && validCurrency(value);
    }

    private boolean validCurrency(String value) {
        try {
            return Currency.getInstance(value) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
