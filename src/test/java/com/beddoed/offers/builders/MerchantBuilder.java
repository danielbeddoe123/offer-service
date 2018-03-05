package com.beddoed.offers.builders;

import com.beddoed.offers.model.Merchant;

import java.util.UUID;

public class MerchantBuilder {

    private UUID merchantId = UUID.randomUUID();

    public MerchantBuilder merchantId(UUID merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public static MerchantBuilder merchantBuilder() {
        return new MerchantBuilder();
    }

    public Merchant build() {
        return new Merchant(merchantId);
    }
}
