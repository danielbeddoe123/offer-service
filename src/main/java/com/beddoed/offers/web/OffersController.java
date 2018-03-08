package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferExpiredException;
import com.beddoed.offers.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.beddoed.offers.web.OfferTransformer.transformModelToResource;
import static com.beddoed.offers.web.OfferTransformer.transformResourceToModel;
import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
public class OffersController {

    private static final String CREATE_OFFER_URI = "/merchandise/{merchandiseId}/offer";
    private static final String GET_OFFER_URI = CREATE_OFFER_URI + "/{offerId}";
    private static final String CANCEL_OFFER_URI = CREATE_OFFER_URI + "/{offerId}";

    private static final Logger LOGGER = LoggerFactory.getLogger(OffersController.class);

    private final OfferService offerService;
    private final MerchandiseService merchandiseService;

    @Autowired
    public OffersController(OfferService offerService, MerchandiseService merchandiseService) {
        this.offerService = offerService;
        this.merchandiseService = merchandiseService;
    }

    @RequestMapping(path = CREATE_OFFER_URI, method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOffer(@PathVariable("merchandiseId") final UUID merchandiseId, @Valid @RequestBody final OfferResource offerResource) {
        LOGGER.info("Received request to create offer: {}", offerResource);
        final Merchandise merchandise = merchandiseService.getMerchandiseById(merchandiseId);
        final Offer offer = transformResourceToModel(offerResource, merchandise);
        UUID offerId = offerService.createOffer(offer);
        return created(getOfferURI(merchandiseId, offerId)).build();
    }

    @RequestMapping(path = GET_OFFER_URI, method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferResource> getOffer(@PathVariable("merchandiseId") final UUID merchandiseId, @PathVariable("offerId") final UUID offerId) {
        LOGGER.info("Received request to get offer: {}", offerId);
        return getOfferResponse(merchandiseId, offerId);
    }

    @RequestMapping(path = CANCEL_OFFER_URI, method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelOffer(@PathVariable("merchandiseId") final UUID merchandiseId, @PathVariable("offerId") final UUID offerId) {
        LOGGER.info("Received request to cancel offer: {}", offerId);
        offerService.cancelOffer(offerId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleUnprocessableEntity(MethodArgumentNotValidException e) {
        final List<org.springframework.validation.FieldError> allErrors = e.getBindingResult().getFieldErrors();
        final List<FieldError> fieldErrors = allErrors.stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());
        LOGGER.warn("Unprocessable entity!", e);
        return ResponseEntity.unprocessableEntity().body(new ApiError(fieldErrors));
    }

    @ExceptionHandler(value = OfferExpiredException.class)
    @ResponseStatus(GONE)
    public void handleOfferExpiredException(OfferExpiredException e) {
        LOGGER.warn("OfferDTO has expired!", e);
    }

    private ResponseEntity<OfferResource> getOfferResponse(final UUID merchandiseId, final UUID offerId) {
        final Offer savedOffer = offerService.getActiveOffer(offerId, merchandiseId);
        if (savedOffer == null) {
            return notFound().build();
        }
        return ok(transformModelToResource(savedOffer));
    }

    private FieldError toFieldError(org.springframework.validation.FieldError error) {
        return new FieldError(error.getField(), error.getCode(), error.getDefaultMessage());
    }

    private URI getOfferURI(final UUID merchandiseId, final UUID offerId) {
        return new UriTemplate(GET_OFFER_URI).expand(merchandiseId, offerId);
    }
}
