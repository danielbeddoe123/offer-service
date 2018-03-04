package com.beddoed.offers.service;

import com.beddoed.offers.model.Offer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

public interface OfferService {

    UUID createOffer(Offer offer);
}
