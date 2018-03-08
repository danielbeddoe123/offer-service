package com.beddoed.offers.service;

import com.beddoed.offers.data.*;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Price.Builder;
import com.beddoed.offers.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final OfferDTO data = transformToDataRepresentation(offer);
        final OfferDTO savedOfferDTO = offerRepository.save(data);
        return savedOfferDTO.getOfferId();
    }

    @Override
    public Offer getActiveOffer(UUID offerId, UUID merchandiseId) {
        final OfferDTO savedOfferDTO = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId);
        if (savedOfferDTO == null || !savedOfferDTO.getActive()) {
            return null;
        }
        return Optional.of(savedOfferDTO)
                .filter(offerDTO -> now().isBefore(offerDTO.getExpiryDate()))
                .map(this::transformToModelRepresentation)
                .orElseThrow(() -> new OfferExpiredException(format("OfferDTO with ID: %s has expired on date: %s", offerId, savedOfferDTO.getExpiryDate())));
    }

    @Override
    @Transactional
    public void cancelOffer(UUID offerId) {
        offerRepository.cancelOffer(offerId);
    }

    private Offer transformToModelRepresentation(OfferDTO offerDTO) {
        return builder()
                .description(offerDTO.getDescription())
                .merchandise(toDomain(offerDTO.getMerchandise()))
                .expiryDate(offerDTO.getExpiryDate())
                .active(offerDTO.getActive())
                .price(Builder.builder()
                        .currency(offerDTO.getCurrencyCode())
                        .amount(offerDTO.getPrice())
                        .build())
                .build();
    }

    private OfferDTO transformToDataRepresentation(Offer offer) {
        final Merchandise merchandise = offer.getMerchandise();
        final MerchantDTO merchant = new MerchantDTO(merchandise.getMerchant().getMerchantId());
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(merchandise.getMerchandiseId(), getType(merchandise), merchant);
        final String currencyCode = offer.getPrice().getCurrency().getCurrencyCode();
        return new OfferDTO(offer.getDescription(), merchandiseDTO, currencyCode, offer.getPrice().getAmount(), offer.getActive(), offer.getExpiryDate());
    }

    private MerchandiseType getType(Merchandise merchandise) {
        if (merchandise instanceof Product) {
            return MerchandiseType.PRODUCT;
        }
        throw new UnsupportedOperationException("MerchandiseDTO type is not supported");
    }
}
