package com.beddoed.offers.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Currency;

import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotAllowPriceToBeCreatedWithoutAmount() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Amount cannot be null");

        // When
        priceBuilder().amount(null).build();
    }

    @Test
    public void shouldNotAllowPriceToBeCreatedWithoutCurrency() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Currency cannot be null");

        // When
        priceBuilder().currency((Currency)null).build();
    }

    @Test
    public void shouldAllowValidPriceToBeCreated() {
        // When
        final Price price = priceBuilder().build();

        // Then
        assertThat(price).isNotNull();
    }

}