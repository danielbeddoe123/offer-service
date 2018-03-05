package com.beddoed.offers.service;

import com.beddoed.offers.builders.OfferBuilder;
import com.beddoed.offers.data.Merchandise;
import com.beddoed.offers.data.MerchandiseType;
import com.beddoed.offers.data.Offer;
import com.beddoed.offers.data.OfferRepository;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.utils.TestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static com.beddoed.offers.builders.MerchandiseBuilder.merchandiseBuilder;
import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static com.beddoed.offers.utils.TestUtils.randomBigDecimal;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldConvertOfferToDataRepresentationAndCallDAO() {
        // Given
        final String description = RandomStringUtils.randomAlphabetic(10);
        final boolean active = TestUtils.randomBoolean();
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = randomUUID();
        final String currencyCode = "GBP";
        final Currency currency = Currency.getInstance(currencyCode);
        final BigDecimal amount = randomBigDecimal();

        final UUID randomOfferId = randomUUID();
        final Offer expectedOfferWithoutId = getExpectedOffer(description, active, merchantId, merchandiseId, currencyCode, amount);
        final Offer expectedOfferWithId = getExpectedOffer(description, active, merchantId, merchandiseId, currencyCode, amount);
        expectedOfferWithId.setOfferId(randomOfferId);
        given(offerRepository.save(expectedOfferWithoutId)).willReturn(expectedOfferWithId);

        // When
        final UUID offerId = offerService.createOffer(offerBuilder()
                .active(active)
                .description(description)
                .expiryDate(LocalDate.now())
                .merchandise(merchandiseBuilder()
                        .merchandiseId(merchandiseId)
                        .merchant(merchant)
                        .buildProduct())
                .price(priceBuilder()
                        .amount(amount)
                        .currency(currency)
                        .build())
                .build());

        // Then
        assertThat(offerId).isEqualTo(randomOfferId);
    }

    @Test
    public void shouldThrowExceptionIfUnsupportedMerchandiseType() {
        // Given
        final com.beddoed.offers.model.Merchandise unexpectedMerchandiseType = new com.beddoed.offers.model.Merchandise(randomUUID(), new Merchant(randomUUID())) {};

        // Expect
        expectedException.expect(UnsupportedOperationException.class);
        expectedException.expectMessage("Merchandise type is not supported");

        // When
        offerService.createOffer(OfferBuilder.offerBuilder().merchandise(unexpectedMerchandiseType).build());
    }

    private Offer getExpectedOffer(String description, boolean active, UUID merchantId, UUID merchandiseId, String currencyCode, BigDecimal amount) {
        return new Offer(description, new Merchandise(merchandiseId, MerchandiseType.PRODUCT, new com.beddoed.offers.data.Merchant(merchantId)),
                currencyCode, amount, active);
    }

}