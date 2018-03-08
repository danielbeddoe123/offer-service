package com.beddoed.offers.service;

import com.beddoed.offers.data.MerchandiseDTO;
import com.beddoed.offers.data.MerchandiseRepository;
import com.beddoed.offers.model.Merchandise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.beddoed.offers.service.MerchandiseTransformer.toDomain;

@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    private final MerchandiseRepository merchandiseRepository;

    @Autowired
    public MerchandiseServiceImpl(MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    @Override
    public Merchandise getMerchandiseById(UUID merchandiseId) {
        final MerchandiseDTO merchandiseDTO = merchandiseRepository.getOne(merchandiseId);
        return toDomain(merchandiseDTO);
    }
}
