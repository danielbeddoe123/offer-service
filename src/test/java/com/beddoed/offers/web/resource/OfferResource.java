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
    private boolean active;

    public OfferResource(String expiryDate, String description, String currencyCode, BigDecimal priceAmount, boolean active) {
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

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * For framework use only
     */
    @Deprecated
    OfferResource() {
    }
}
