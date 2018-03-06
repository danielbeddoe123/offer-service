package com.beddoed.offers.web;

import com.beddoed.offers.builders.MerchandiseBuilder;
import com.beddoed.offers.builders.OfferBuilder;
import com.beddoed.offers.builders.PriceBuilder;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;
import com.beddoed.offers.utils.TestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;

import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static com.beddoed.offers.utils.TestUtils.randomBigDecimal;
import static com.beddoed.offers.utils.TestUtils.randomBoolean;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

public class OfferTransformerTest {

    @Test
    public void shouldTransformResourceToModel() {
        // Given
        final boolean active = randomBoolean();
        final String currencyCode = "GBP";
        final String description = randomAlphanumeric(10);
        final LocalDate expiryDate = LocalDate.now();
        final BigDecimal priceAmount = randomBigDecimal();
        final Merchandise merchandise = MerchandiseBuilder.merchandiseBuilder().buildProduct();
        final OfferResource offerResource = new OfferResource();
        offerResource.setActive(active);
        offerResource.setCurrencyCode(currencyCode);
        offerResource.setDescription(description);
        offerResource.setExpiryDate(expiryDate);
        offerResource.setPriceAmount(priceAmount);

        final Price expectedPrice = priceBuilder().currency(Currency.getInstance(currencyCode)).amount(priceAmount).build();
        final Offer expectedOffer = offerBuilder()
                .active(active)
                .description(description)
                .expiryDate(expiryDate)
                .merchandise(merchandise)
                .price(expectedPrice)
                .build();

        // When
        final Offer offer = OfferTransformer.transformResource(offerResource, merchandise);

        // Then
        assertThat(offer).isEqualTo(expectedOffer);
    }
}