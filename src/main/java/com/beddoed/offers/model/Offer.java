package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public class Offer {

    private final Merchandise merchandise;
    private final LocalDate expiryDate;
    private final String description;
    private final Price price;
    private final Boolean active;

    public Offer(Merchandise merchandise, LocalDate expiryDate, String description, Price price, Boolean active) {
        this.merchandise = merchandise;
        this.expiryDate = expiryDate;
        this.description = description;
        this.price = price;
        this.active = active;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public Boolean getActive() {
        return active;
    }
}
