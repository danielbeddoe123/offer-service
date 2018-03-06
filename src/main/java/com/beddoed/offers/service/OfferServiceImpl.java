package com.beddoed.offers.service;

import com.beddoed.offers.data.*;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public UUID createOffer(Offer offer) {
        validate(offer);
        final com.beddoed.offers.data.Offer data = transformToDataRepresentation(offer);
        final com.beddoed.offers.data.Offer savedOffer = offerRepository.save(data);
        return savedOffer.getOfferId();
    }

    @Override
    public Offer getOffer(UUID offerId, UUID merchandiseId) {
        return null;
    }

    private com.beddoed.offers.data.Offer transformToDataRepresentation(Offer offer) {
        final com.beddoed.offers.model.Merchandise merchandise = offer.getMerchandise();
        final Merchant merchant = new Merchant(merchandise.getMerchant().getMerchantId());
        final Merchandise merchandiseData = new Merchandise(merchandise.getMerchandiseId(), getType(merchandise), merchant);
        final String currencyCode = offer.getPrice().getCurrency().getCurrencyCode();
        return new com.beddoed.offers.data.Offer(offer.getDescription(), merchandiseData, currencyCode, offer.getPrice().getAmount(), offer.getActive());
    }

    private void validate(Offer offer) {
        if (offer.getMerchandise() == null || offer.getMerchandise().getMerchandiseId() == null) {
            throw new IllegalArgumentException("There is no merchandise to create an offer for");
        }
    }

    private MerchandiseType getType(com.beddoed.offers.model.Merchandise merchandise) {
        if (merchandise instanceof Product) {
            return MerchandiseType.PRODUCT;
        }
        throw new UnsupportedOperationException("Merchandise type is not supported");
    }
}
