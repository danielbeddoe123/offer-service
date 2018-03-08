package com.beddoed.offers.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static com.beddoed.offers.builders.MerchantBuilder.merchantBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class MerchantTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotAllowMerchantToBeCreatedWithoutId() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Merchant ID cannot be null");

        // When
        merchantBuilder().merchantId(null).build();
    }

    @Test
    public void shouldAllowValidMerchantToBeCreated() {
        // Expect

        // When
        final Merchant merchant = merchantBuilder().merchantId(UUID.randomUUID()).build();

        // Then
        assertThat(merchant).isNotNull();
    }
}