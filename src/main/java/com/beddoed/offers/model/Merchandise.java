package com.beddoed.offers.model;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public abstract class Merchandise {

    private final UUID merchandiseId;
    private final Merchant merchant;

    protected Merchandise(UUID merchandiseId, Merchant merchant) {
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
