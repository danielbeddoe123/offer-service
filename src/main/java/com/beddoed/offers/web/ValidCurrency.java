package com.beddoed.offers.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {

    String message() default "Currency code is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
