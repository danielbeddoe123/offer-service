package com.beddoed.offers.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@ToString
public class Merchandise {

    @Id
    @GeneratedValue
    private UUID merchandiseId;

    @Enumerated(EnumType.STRING)
    private MerchandiseType merchandiseType;

    @ManyToOne
    private Merchant merchant;

    public Merchandise(UUID merchandiseId, MerchandiseType merchandiseType, Merchant merchant) {
        this.merchandiseId = merchandiseId;
        this.merchandiseType = merchandiseType;
        this.merchant = merchant;
    }

    public UUID getMerchandiseId() {
        return merchandiseId;
    }

    public MerchandiseType getMerchandiseType() {
        return merchandiseType;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchandiseId(UUID merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public void setMerchandiseType(MerchandiseType merchandiseType) {
        this.merchandiseType = merchandiseType;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * For framework use only
     */
    @Deprecated
    Merchandise() {
    }
}
