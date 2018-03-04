package com.beddoed.offers.web;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.*;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class OfferRequestDataFactory {

    public static final OfferRequest ACTIVE_OFFER_REQUEST = new OfferRequest(now().plusWeeks(1).format(ISO_LOCAL_DATE), "Something", "GBP", BigDecimal.TEN, true);

    private static Gson gson = new GsonBuilder().create();

    public static String toJson(String expiryDate, String description, String currencyCode, BigDecimal amount, boolean active) {
        final OfferRequest offerRequest = new OfferRequest(expiryDate, description, currencyCode, amount, active);
        return gson.toJson(offerRequest);
    }

    public static String toJson(OfferRequest offerRequest) {
        return gson.toJson(offerRequest);
    }
}
