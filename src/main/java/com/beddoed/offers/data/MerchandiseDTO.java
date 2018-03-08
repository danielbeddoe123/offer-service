package com.beddoed.offers.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@ToString
@Table(name = "merchandise")
public class MerchandiseDTO {

    @Id
    @GeneratedValue
    private UUID merchandiseId;

    @Enumerated(EnumType.STRING)
    private MerchandiseType merchandiseType;

    @ManyToOne
    private MerchantDTO merchant;

    public MerchandiseDTO(UUID merchandiseId, MerchandiseType merchandiseType, MerchantDTO merchant) {
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

    public MerchantDTO getMerchant() {
        return merchant;
    }

    public void setMerchandiseId(UUID merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public void setMerchandiseType(MerchandiseType merchandiseType) {
        this.merchandiseType = merchandiseType;
    }

    public void setMerchant(MerchantDTO merchant) {
        this.merchant = merchant;
    }

    /**
     * For framework use only
     */
    @Deprecated
    MerchandiseDTO() {
    }
}
