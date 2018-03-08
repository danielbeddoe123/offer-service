package com.beddoed.offers.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class OfferTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotAllowOfferToBeCreatedWithoutMerchandise() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Merchandise cannot be null");

        // When
        offerBuilder().merchandise(null).build();
    }

    @Test
    public void shouldNotAllowOfferToBeCreatedWithoutExpiryDate() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Expiry date cannot be null");

        // When
        offerBuilder().expiryDate(null).build();
    }

    @Test
    public void shouldNotAllowOfferToBeCreatedWithoutPrice() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Price cannot be null");

        // When
        offerBuilder().price(null).build();
    }

    @Test
    public void shouldNotAllowOfferToBeCreatedWithoutDescription() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Description cannot be null");

        // When
        offerBuilder().description(null).build();
    }

    @Test
    public void shouldNotAllowOfferToBeCreatedWithoutActive() {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Active cannot be null");

        // When
        offerBuilder().active(null).build();
    }

    @Test
    public void shouldAllowValidOfferToBeCreated() {
        // When
        final Offer offer = offerBuilder().build();

        // Then
        assertThat(offer).isNotNull();
    }
}