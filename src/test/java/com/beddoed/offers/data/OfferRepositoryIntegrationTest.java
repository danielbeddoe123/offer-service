package com.beddoed.offers.data;

import com.beddoed.Application;
import com.beddoed.offers.model.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OfferRepositoryIntegrationTest {

    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void shouldSaveAndFindOfferInDatabase() {
        // Given
        final MerchantDTO merchantDTO = new MerchantDTO(UUID.randomUUID());
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(UUID.randomUUID(), MerchandiseType.PRODUCT, merchantDTO);
        final Price price = new Price(Currency.getInstance("GBP"), BigDecimal.TEN);
        final OfferDTO offer = new OfferDTO("test", merchandiseDTO, price, true);

        // When
        OfferDTO offerDTO = offerRepository
                .save(offer);

        // Then
        assertThat(offerDTO.getOfferId()).isNotNull();
        OfferDTO foundOffer = offerRepository
                .findOne(offerDTO.getOfferId());
        assertThat(foundOffer).isNotNull();
        assertThat(offerDTO).isEqualTo(foundOffer);
    }
}
