package com.beddoed.offers.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class Product extends Merchandise {
    public Product(UUID merchandiseId, Merchant merchant) {
        super(merchandiseId, merchant);
    }

    static class Builder {
        private UUID merchandiseId;
        private Merchant merchant;

        public static Builder builder() {
            return new Builder();
        }

        public Builder setMerchandiseId(UUID merchandiseId) {
            this.merchandiseId = merchandiseId;
            return this;
        }

        public Builder setMerchant(Merchant merchant) {
            this.merchant = merchant;
            return this;
        }

        public Merchandise build() {
            requireNonNull(merchandiseId, "MerchandiseDTO ID cannot be null");
            requireNonNull(merchant, "MerchantDTO cannot be null");
            return new Product(merchandiseId, merchant);
        }
    }
}
