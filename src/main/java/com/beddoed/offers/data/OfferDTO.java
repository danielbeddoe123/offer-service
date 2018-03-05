package com.beddoed.offers.data;

import com.beddoed.offers.model.Price;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@EqualsAndHashCode
public class OfferDTO {

    @Id
    @GeneratedValue
    private UUID offerId;

    private String description;

    private MerchandiseDTO merchandiseDTO;
    private Price price;
    private Boolean active;

    public OfferDTO(UUID offerId, String description, Boolean active) {
        this.offerId = offerId;
        this.description = description;
        this.merchandiseDTO = merchandiseDTO;
        this.price = price;
        this.active = active;
    }

    public OfferDTO(String description, MerchandiseDTO merchandiseDTO, Price price, Boolean active) {
        this.description = description;
        this.merchandiseDTO = merchandiseDTO;
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

    public MerchandiseDTO getMerchandiseDTO() {
        return merchandiseDTO;
    }

    public void setMerchandiseDTO(MerchandiseDTO merchandiseDTO) {
        this.merchandiseDTO = merchandiseDTO;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * For framework use only
     */
    @Deprecated
    OfferDTO() {
    }
}
