package com.beddoed.offers.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@ToString
@Table(name = "merchant")
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
