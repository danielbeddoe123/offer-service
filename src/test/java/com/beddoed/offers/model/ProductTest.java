package com.beddoed.offers.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.beddoed.offers.builders.MerchandiseBuilder.merchandiseBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotAllowProductToBeCreatedWithoutId() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Merchandise ID cannot be null");

        // When
        merchandiseBuilder().merchandiseId(null).buildProduct();
    }

    @Test
    public void shouldNotAllowProductToBeCreatedWithoutMerchant() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Merchant cannot be null");

        // When
        merchandiseBuilder().merchant(null).buildProduct();
    }

    @Test
    public void shouldAllowValidProductToBeCreated() {
        // Expect

        // When
        final Merchandise merchandise = merchandiseBuilder().buildProduct();

        // Then
        assertThat(merchandise).isNotNull();
    }
}