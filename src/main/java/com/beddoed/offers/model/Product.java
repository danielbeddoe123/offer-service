package com.beddoed.offers.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Product extends Merchandise {
    private Product(UUID merchandiseId, Merchant merchant) {
        super(merchandiseId, merchant);
    }

    public static class Builder {
        private UUID merchandiseId;
        private Merchant merchant;

        public static Builder builder() {
            return new Builder();
        }

        public Builder merchandiseId(UUID merchandiseId) {
            this.merchandiseId = merchandiseId;
            return this;
        }

        public Builder merchant(Merchant merchant) {
            this.merchant = merchant;
            return this;
        }

        public Merchandise build() {
            requireNonNull(merchandiseId, "Merchandise ID cannot be null");
            requireNonNull(merchant, "Merchant cannot be null");
            return new Product(merchandiseId, merchant);
        }
    }
}
