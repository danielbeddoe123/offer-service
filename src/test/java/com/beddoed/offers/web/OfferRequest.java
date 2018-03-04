package com.beddoed.offers.web;

import com.google.gson.GsonBuilder;

import java.math.BigDecimal;

public class OfferRequest {
    private final String expiryDate;
    private final String description;
    private final String currencyCode;
    private final BigDecimal priceAmount;
    private final boolean active;

    public OfferRequest(String expiryDate, String description, String currencyCode, BigDecimal priceAmount, boolean active) {
        this.expiryDate = expiryDate;
        this.description = description;
        this.currencyCode = currencyCode;
        this.priceAmount = priceAmount;
        this.active = active;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public boolean isActive() {
        return active;
    }
}
