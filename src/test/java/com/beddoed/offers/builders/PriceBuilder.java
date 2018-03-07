package com.beddoed.offers.builders;

import com.beddoed.offers.model.Price;
import com.beddoed.offers.utils.TestUtils;

import java.math.BigDecimal;
import java.util.Currency;

import static com.beddoed.offers.model.Price.Builder.*;
import static com.beddoed.offers.utils.TestUtils.randomBigDecimal;
import static java.util.Currency.getInstance;

public class PriceBuilder {
    private Currency currency = getInstance("GBP");
    private BigDecimal amount = randomBigDecimal();

    public static PriceBuilder priceBuilder() {
        return new PriceBuilder();
    }

    public Price build() {
        return builder()
                .currency(currency)
                .amount(amount)
                .build();
    }

    public PriceBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public PriceBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PriceBuilder currency(String currencyCode) {
        return currency(Currency.getInstance(currencyCode));
    }
}
