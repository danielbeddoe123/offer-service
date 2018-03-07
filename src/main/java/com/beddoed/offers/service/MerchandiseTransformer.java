package com.beddoed.offers.service;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Product;

public class MerchandiseTransformer {

    public static Merchandise toDomain(com.beddoed.offers.data.Merchandise merchandiseDTO) {
        switch (merchandiseDTO.getMerchandiseType()) {
            case PRODUCT:
                return new Product(merchandiseDTO.getMerchandiseId(), getMerchant(merchandiseDTO.getMerchant()));
            default:
                throw new UnsupportedOperationException("Merchandise type not supported");
        }
    }

    private static Merchant getMerchant(com.beddoed.offers.data.Merchant merchant) {
        return new Merchant(merchant.getMerchantId());
    }
}
