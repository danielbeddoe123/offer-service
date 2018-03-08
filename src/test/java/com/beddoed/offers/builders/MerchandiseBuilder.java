package com.beddoed.offers.builders;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Product;

import java.util.UUID;

import static com.beddoed.offers.builders.MerchantBuilder.merchantBuilder;

public class MerchandiseBuilder {

    private UUID merchandiseId = UUID.randomUUID();
    private Merchant merchant = merchantBuilder().build();

    public MerchandiseBuilder merchandiseId(UUID merchandiseId) {
        this.merchandiseId = merchandiseId;
        return this;
    }

    public MerchandiseBuilder merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public static MerchandiseBuilder merchandiseBuilder() {
        return new MerchandiseBuilder();
    }

    public Merchandise buildProduct() {
        return Product.Builder
                .builder()
                .merchant(merchant)
                .merchandiseId(merchandiseId)
                .build();
    }
}
