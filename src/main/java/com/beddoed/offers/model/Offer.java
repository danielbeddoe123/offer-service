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

    private Offer(Merchandise merchandise, LocalDate expiryDate, String description, Price price, Boolean active) {
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

    public static class Builder {

        private Merchandise merchandise;
        private LocalDate expiryDate;
        private String description;
        private Price price;
        private Boolean active;

        public static Builder builder() {
            return new Builder();
        }

        public Builder merchandise(Merchandise merchandise) {
            this.merchandise = merchandise;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Price price) {
            this.price = price;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }


        public Offer build() {
            return new Offer(merchandise, expiryDate, description, price, active);
        }
    }
}
