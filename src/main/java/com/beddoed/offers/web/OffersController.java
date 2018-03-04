package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.UUID;

import static com.beddoed.offers.web.OfferTransformer.transformResource;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
public class OffersController {

    private static final String CREATE_OFFER_URI = "/merchandise/{merchandiseId}/offer";
    private static final String GET_OFFER_URI = CREATE_OFFER_URI + "/{offerId}";

    private static final Logger LOGGER = LoggerFactory.getLogger(OffersController.class);

    private final OfferService offerService;
    private final MerchandiseService merchandiseService;

    @Autowired
    public OffersController(OfferService offerService, MerchandiseService merchandiseService) {
        this.offerService = offerService;
        this.merchandiseService = merchandiseService;
    }

    @RequestMapping(path = CREATE_OFFER_URI, method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOffer(@PathVariable("merchandiseId") final UUID merchandiseId, @RequestBody final OfferResource offerResource) {
        LOGGER.info("Received request to create offer: {}", offerResource);
        validate(offerResource);

        final Merchandise merchandise = merchandiseService.getMerchandiseById(merchandiseId);
        final Offer offer = transformResource(offerResource, merchandise);
        UUID offerId = offerService.createOffer(offer);
        return created(getOfferURI(merchandiseId, offerId)).build();
    }


    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleException(IllegalStateException e) {
        LOGGER.warn("Unprocessable entity!", e);
    }

    private void validate(OfferResource offerResource) {
        if (offerResource.getCurrencyCode() == null) {
            throw new IllegalArgumentException("Currency Code cannot be null");
        }
    }

    private URI getOfferURI(final UUID merchandiseId, final UUID offerId) {
        return new UriTemplate(GET_OFFER_URI).expand(merchandiseId, offerId);
    }
}
