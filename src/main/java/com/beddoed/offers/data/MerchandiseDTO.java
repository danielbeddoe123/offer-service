package com.beddoed.offers.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MerchandiseDTO {

    @Id
    @GeneratedValue
    private UUID merchandiseId;

    private MerchandiseType merchandiseType;
    private MerchantDTO merchantDTO;

    public MerchandiseDTO(UUID merchandiseId, MerchandiseType merchandiseType, MerchantDTO merchantDTO) {
        this.merchandiseId = merchandiseId;
        this.merchandiseType = merchandiseType;
        this.merchantDTO = merchantDTO;
    }

    public UUID getMerchandiseId() {
        return merchandiseId;
    }

    public MerchandiseType getMerchandiseType() {
        return merchandiseType;
    }

    public MerchantDTO getMerchantDTO() {
        return merchantDTO;
    }

    public void setMerchandiseId(UUID merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    public void setMerchandiseType(MerchandiseType merchandiseType) {
        this.merchandiseType = merchandiseType;
    }

    public void setMerchantDTO(MerchantDTO merchantDTO) {
        this.merchantDTO = merchantDTO;
    }

    /**
     * For framework use only
     */
    @Deprecated
    MerchandiseDTO() {
    }
}
