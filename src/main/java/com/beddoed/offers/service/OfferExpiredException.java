package com.beddoed.offers.service;

public class OfferExpiredException extends RuntimeException {
    public OfferExpiredException(String message) {
        super(message);
    }
}
