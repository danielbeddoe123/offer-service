package com.beddoed.offers.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MerchantDTO {

    @Id
    @GeneratedValue
    private UUID merchantId;

    public MerchantDTO(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(UUID merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * For framework use only
     */
    @Deprecated
    MerchantDTO() {
    }
}
