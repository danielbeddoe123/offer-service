package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public class Price {

    private final Currency currency;
    private final BigDecimal amount;

    public Price(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
