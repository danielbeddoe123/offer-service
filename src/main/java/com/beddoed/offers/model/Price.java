package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import static java.util.Currency.getInstance;
import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
@ToString
public class Price {

    private final Currency currency;
    private final BigDecimal amount;

    private Price(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static class Builder {

        private Currency currency;
        private BigDecimal amount;

        public static Builder builder() {
            return new Builder();
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder currency(String currencyCode) {
            this.currency = getInstance(currencyCode);
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Price build() {
            requireNonNull(currency, "Currency cannot be null");
            requireNonNull(amount, "Amount cannot be null");
            return new Price(currency, amount);
        }
    }
}
