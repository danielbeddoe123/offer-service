package com.beddoed.offers.builders;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Random;

import static com.beddoed.offers.model.Offer.Builder.builder;

public class OfferBuilder {

    private Merchandise merchandise = null;
    private LocalDate expiryDate = LocalDate.now();
    private String description = RandomStringUtils.random(10);
    private Price price = PriceBuilder.priceBuilder().build();
    private Boolean active = new Random().nextBoolean();

    public static OfferBuilder offerBuilder() {
        return new OfferBuilder();
    }

    public Offer build() {
        return builder()
                .merchandise(merchandise)
                .expiryDate(expiryDate)
                .description(description)
                .price(price)
                .active(active)
                .build();
    }

    public OfferBuilder expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public OfferBuilder merchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
        return this;
    }

    public OfferBuilder description(String description) {
        this.description = description;
        return this;
    }

    public OfferBuilder price(Price price) {
        this.price = price;
        return this;
    }

    public OfferBuilder active(Boolean active) {
        this.active = active;
        return this;
    }
}
