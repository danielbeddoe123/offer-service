package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public abstract class Merchandise {

    private final UUID merchandiseId;
    private final Merchant merchant;

    public Merchandise(UUID merchandiseId, Merchant merchant) {
        this.merchandiseId = merchandiseId;
        this.merchant = merchant;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public UUID getMerchandiseId() {
        return merchandiseId;
    }
}
