package com.beddoed.offers.web.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.gson.GsonBuilder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode
public class OfferResource {

    private String expiryDate;
    private String description;
    private String currencyCode;
    private BigDecimal priceAmount;

    public OfferResource(String expiryDate, String description, String currencyCode, BigDecimal priceAmount) {
        this.expiryDate = expiryDate;
        this.description = description;
        this.currencyCode = currencyCode;
        this.priceAmount = priceAmount;
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

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    /**
     * For framework use only
     */
    @Deprecated
    OfferResource() {
    }
}
