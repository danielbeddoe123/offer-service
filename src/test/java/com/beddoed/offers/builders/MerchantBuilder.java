package com.beddoed.offers.builders;

import com.beddoed.offers.model.Merchant;

import java.util.UUID;

import static com.beddoed.offers.model.Merchant.Builder.builder;

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
        return builder().merchantId(merchantId).build();
    }
}
