package com.beddoed.offers.service;

import com.beddoed.offers.data.MerchandiseRepository;
import com.beddoed.offers.data.Merchant;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    private final MerchandiseRepository merchandiseRepository;

    @Autowired
    public MerchandiseServiceImpl(MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    @Override
    public Merchandise getMerchandiseById(UUID merchandiseId) {
        final com.beddoed.offers.data.Merchandise merchandiseDTO = merchandiseRepository.getOne(merchandiseId);
        return getMerchandise(merchandiseDTO);
    }

    private Merchandise getMerchandise(com.beddoed.offers.data.Merchandise merchandiseDTO) {
        switch (merchandiseDTO.getMerchandiseType()) {
            case PRODUCT:
                return new Product(merchandiseDTO.getMerchandiseId(), getMerchant(merchandiseDTO.getMerchant()));
            default:
                throw new UnsupportedOperationException("Merchandise type not supported");
        }
    }

    private com.beddoed.offers.model.Merchant getMerchant(Merchant merchant) {
        return new com.beddoed.offers.model.Merchant(merchant.getMerchantId());
    }
}
