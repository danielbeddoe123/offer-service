package com.beddoed.offers.service;

import com.beddoed.offers.model.Merchandise;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface MerchandiseService {

    Merchandise getMerchandiseById(UUID merchandiseId);
}
