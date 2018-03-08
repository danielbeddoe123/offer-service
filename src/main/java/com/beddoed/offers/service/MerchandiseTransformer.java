package com.beddoed.offers.service;

import com.beddoed.offers.data.MerchandiseDTO;
import com.beddoed.offers.data.MerchantDTO;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Merchant.Builder;

import static com.beddoed.offers.model.Product.Builder.builder;

public class MerchandiseTransformer {

    public static Merchandise toDomain(MerchandiseDTO merchandiseDTO) {
        switch (merchandiseDTO.getMerchandiseType()) {
            case PRODUCT:
                return builder()
                        .merchant(getMerchant(merchandiseDTO.getMerchant()))
                        .merchandiseId(merchandiseDTO.getMerchandiseId())
                        .build();
            default:
                throw new UnsupportedOperationException("MerchandiseDTO type not supported");
        }
    }

    private static Merchant getMerchant(MerchantDTO merchant) {
        return Builder.builder()
                .merchantId(merchant.getMerchantId())
                .build();
    }
}
