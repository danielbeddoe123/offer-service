package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class Merchant {
    private final UUID merchantId;

    public Merchant(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public UUID getMerchantId() {
        return merchantId;
    }
}
