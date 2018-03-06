package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;

import java.util.Currency;

public class OfferTransformer {

    public static Offer transformResourceToModel(OfferResource offerResource, Merchandise merchandise) {
        final Price price = new Price(Currency.getInstance(offerResource.getCurrencyCode()), offerResource.getPriceAmount());
        return new Offer(merchandise, offerResource.getExpiryDate(),
                offerResource.getDescription(),
                price, offerResource.isActive());
    }

    public static OfferResource transformModelToResource(Offer offer) {
        final OfferResource offerResource = new OfferResource();
        offerResource.setPriceAmount(offer.getPrice().getAmount());
        offerResource.setExpiryDate(offer.getExpiryDate());
        offerResource.setCurrencyCode(offer.getPrice().getCurrency().getCurrencyCode());
        offerResource.setDescription(offer.getDescription());
        offerResource.setActive(offer.getActive());
        return offerResource;
    }
}
