package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class Merchandise {
    private final Merchant merchant;

    public Merchandise(Merchant merchant) {
        this.merchant = merchant;
    }

    public Merchant getMerchant() {
        return merchant;
    }
}
