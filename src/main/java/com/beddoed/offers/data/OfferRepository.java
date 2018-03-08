package com.beddoed.offers.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    Offer findByOfferIdAndMerchandise_MerchandiseId(UUID offerId, UUID merchandiseId);

    @Modifying
    @Query("update Offer o set o.active = false where o.offerId = ?1")
    void updateOfferAsCancelled(UUID offerId);
}
