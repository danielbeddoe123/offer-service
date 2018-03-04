package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;

import java.util.Currency;

public class OfferTransformer {

    public static Offer transformResource(OfferResource offerResource, Merchandise merchandise) {
        final Price price = new Price(Currency.getInstance(offerResource.getCurrencyCode()), offerResource.getPriceAmount());
        return new Offer(merchandise, offerResource.getExpiryDate(),
                offerResource.getDescription(),
                price, offerResource.isActive());
    }
}
