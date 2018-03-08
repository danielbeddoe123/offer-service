package com.beddoed.offers.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@EqualsAndHashCode
@ToString
@Table(name = "offer")
public class OfferDTO {

    @Id
    @GeneratedValue
    private UUID offerId;

    private String description;

    @ManyToOne
    private MerchandiseDTO merchandise;

    private String currencyCode;

    private BigDecimal price;

    private Boolean active;

    private LocalDate expiryDate;

    public OfferDTO(String description, MerchandiseDTO merchandise, String currencyCode, BigDecimal price, Boolean active, LocalDate expiryDate) {
        this.description = description;
        this.merchandise = merchandise;
        this.currencyCode = currencyCode;
        this.price = price;
        this.active = active;
        this.expiryDate = expiryDate;
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

    public MerchandiseDTO getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(MerchandiseDTO merchandise) {
        this.merchandise = merchandise;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * For framework use only
     */
    @Deprecated
    OfferDTO() {
    }
}
