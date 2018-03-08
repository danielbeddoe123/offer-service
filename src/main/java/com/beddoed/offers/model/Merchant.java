package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public class Merchant {
    private final UUID merchantId;

    private Merchant(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public static class Builder {

        private UUID merchantId;

        public static Builder builder() {
            return new Builder();
        }

        public Builder merchantId(UUID merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Merchant build() {
            requireNonNull(merchantId, "Merchant ID cannot be null");
            return new Merchant(merchantId);
        }
    }
}
