package com.beddoed.offers.service;

import com.beddoed.offers.data.MerchandiseDTO;
import com.beddoed.offers.data.MerchantDTO;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Product;

public class MerchandiseTransformer {

    public static Merchandise toDomain(MerchandiseDTO merchandiseDTO) {
        switch (merchandiseDTO.getMerchandiseType()) {
            case PRODUCT:
                return new Product(merchandiseDTO.getMerchandiseId(), getMerchant(merchandiseDTO.getMerchant()));
            default:
                throw new UnsupportedOperationException("MerchandiseDTO type not supported");
        }
    }

    private static Merchant getMerchant(MerchantDTO merchant) {
        return new Merchant(merchant.getMerchantId());
    }
}
