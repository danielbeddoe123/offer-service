package com.beddoed.offers.model;

import java.util.UUID;

public class Product extends Merchandise {
    public Product(UUID merchandiseId, Merchant merchant) {
        super(merchandiseId, merchant);
    }
}
