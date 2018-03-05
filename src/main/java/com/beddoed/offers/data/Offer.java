package com.beddoed.offers.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@ToString
public class Offer {

    @Id
    @GeneratedValue
    private UUID offerId;

    private String description;

    @ManyToOne
    private Merchandise merchandise;

    @ManyToOne
    private Currency currency;

    private BigDecimal price;

    private Boolean active;

    public Offer(String description, Merchandise merchandise, Currency currency, BigDecimal price, Boolean active) {
        this.description = description;
        this.merchandise = merchandise;
        this.currency = currency;
        this.price = price;
        this.active = active;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public void setOfferId(UUID offerId) {
        this.offerId = offerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * For framework use only
     */
    @Deprecated
    Offer() {
    }
}
