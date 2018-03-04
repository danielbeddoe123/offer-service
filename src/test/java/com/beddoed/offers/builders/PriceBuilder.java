package com.beddoed.offers.builders;

import com.beddoed.offers.model.Price;

import java.math.BigDecimal;
import java.util.Currency;

public class PriceBuilder {
    private Currency currency;
    private BigDecimal amount;

    public static PriceBuilder priceBuilder() {
        return new PriceBuilder();
    }

    public Price build() {
        return new Price(currency, amount);
    }

    public PriceBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public PriceBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
