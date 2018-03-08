package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;
import com.beddoed.offers.model.Price.Builder;

import java.util.Currency;

public class OfferTransformer {

    public static Offer transformResourceToModel(OfferResource offerResource, Merchandise merchandise) {
        final Price price = Builder.builder()
                .currency(offerResource.getCurrencyCode())
                .amount(offerResource.getPriceAmount())
                .build();

        return Offer.Builder.builder()
                .price(price)
                .merchandise(merchandise)
                .expiryDate(offerResource.getExpiryDate())
                .active(true)
                .description(offerResource.getDescription())
                .build();
    }

    public static OfferResource transformModelToResource(Offer offer) {
        final OfferResource offerResource = new OfferResource();
        offerResource.setPriceAmount(offer.getPrice().getAmount());
        offerResource.setExpiryDate(offer.getExpiryDate());
        offerResource.setCurrencyCode(offer.getPrice().getCurrency().getCurrencyCode());
        offerResource.setDescription(offer.getDescription());
        return offerResource;
    }
}
