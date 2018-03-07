package com.beddoed.offers.service;

import com.beddoed.offers.data.*;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price;
import com.beddoed.offers.model.Price.Builder;
import com.beddoed.offers.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.beddoed.offers.model.Offer.Builder.builder;
import static com.beddoed.offers.service.MerchandiseTransformer.toDomain;
import static java.lang.String.format;
import static java.time.LocalDate.now;

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
    public Offer getActiveOffer(UUID offerId, UUID merchandiseId) {
        final com.beddoed.offers.data.Offer savedOffer = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId);
        if (savedOffer == null) {
            return null;
        }
        return Optional.of(savedOffer)
                .filter(offer -> now().isBefore(offer.getExpiryDate()))
                .map(this::transformToModelRepresentation)
                .orElseThrow(() -> new OfferExpiredException(format("Offer with ID: %s has expired on date: %s", offerId, savedOffer.getExpiryDate())));
    }

    private Offer transformToModelRepresentation(com.beddoed.offers.data.Offer offer) {
        return builder()
                .description(offer.getDescription())
                .merchandise(toDomain(offer.getMerchandise()))
                .expiryDate(offer.getExpiryDate())
                .active(offer.getActive())
                .price(Builder.builder()
                        .currency(offer.getCurrencyCode())
                        .amount(offer.getPrice())
                        .build())
                .build();
    }

    private com.beddoed.offers.data.Offer transformToDataRepresentation(Offer offer) {
        final com.beddoed.offers.model.Merchandise merchandise = offer.getMerchandise();
        final Merchant merchant = new Merchant(merchandise.getMerchant().getMerchantId());
        final Merchandise merchandiseData = new Merchandise(merchandise.getMerchandiseId(), getType(merchandise), merchant);
        final String currencyCode = offer.getPrice().getCurrency().getCurrencyCode();
        return new com.beddoed.offers.data.Offer(offer.getDescription(), merchandiseData, currencyCode, offer.getPrice().getAmount(), offer.getActive(), offer.getExpiryDate());
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
