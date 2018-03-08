package com.beddoed.offers.service;

import com.beddoed.offers.model.Offer;

import java.util.UUID;

public interface OfferService {

    UUID createOffer(Offer offer);

    Offer getActiveOffer(UUID offerId, UUID merchandiseId);

    void cancelOffer(UUID offerId, UUID merchandiseId);
}
