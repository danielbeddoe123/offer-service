package com.beddoed.offers.web.resource;

import com.google.gson.*;

import java.math.BigDecimal;

import static java.time.LocalDate.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class OfferResourceDataFactory {

    public static final OfferResource ACTIVE_OFFER_REQUEST = new OfferResource(now().plusWeeks(1).format(ISO_LOCAL_DATE), "Something", "GBP", BigDecimal.TEN, true);

    private static Gson gson = new GsonBuilder().create();

    public static String toJson(String expiryDate, String description, String currencyCode, BigDecimal amount, boolean active) {
        final OfferResource offerResource = new OfferResource(expiryDate, description, currencyCode, amount, active);
        return gson.toJson(offerResource);
    }

    public static String toJson(OfferResource offerResource) {
        return gson.toJson(offerResource);
    }
}
