package com.beddoed.offers.service;

import com.beddoed.offers.builders.MerchandiseBuilder;
import com.beddoed.offers.builders.PriceBuilder;
import com.beddoed.offers.data.*;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Price;
import com.beddoed.offers.utils.TestUtils;
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
import static com.beddoed.offers.builders.MerchantBuilder.merchantBuilder;
import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static com.beddoed.offers.utils.TestUtils.randomBigDecimal;
import static com.beddoed.offers.utils.TestUtils.randomBoolean;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        final String description = randomAlphabetic(10);
        final boolean active = TestUtils.randomBoolean();
        final UUID merchantId = randomUUID();
        final Merchant merchant = merchantBuilder().merchantId(merchantId).build();
        final UUID merchandiseId = randomUUID();
        final String currencyCode = "GBP";
        final Currency currency = Currency.getInstance(currencyCode);
        final BigDecimal amount = randomBigDecimal();

        final UUID randomOfferId = randomUUID();
        final LocalDate expiryDate = LocalDate.now();
        final OfferDTO expectedOfferWithoutId = getOfferDTO(description, active, merchantId, merchandiseId, currencyCode, amount, expiryDate);
        final OfferDTO expectedOfferWithId = getOfferDTO(description, active, merchantId, merchandiseId, currencyCode, amount, expiryDate);
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
        final com.beddoed.offers.model.Merchandise unexpectedMerchandiseType = new com.beddoed.offers.model.Merchandise(randomUUID(), merchantBuilder().build()) {};

        // Expect
        expectedException.expect(UnsupportedOperationException.class);
        expectedException.expectMessage("MerchandiseDTO type is not supported");

        // When
        offerService.createOffer(offerBuilder().merchandise(unexpectedMerchandiseType).build());
    }

    @Test
    public void shouldFetchActiveOffer() {
        // Given
        final UUID offerId = randomUUID();
        final UUID merchandiseId = randomUUID();
        final UUID merchantId = randomUUID();
        final String currencyCode = "GBP";
        final boolean active = true;
        final BigDecimal priceAmount = BigDecimal.TEN;
        final LocalDate expiryDate = LocalDate.now().plusDays(1);
        final String description = randomAlphabetic(10);
        final OfferDTO offerDTO = getOfferDTO(description, active, merchantId, merchandiseId, currencyCode, priceAmount, expiryDate);
        given(offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId)).willReturn(offerDTO);

        // When
        final com.beddoed.offers.model.Offer activeOffer = offerService.getActiveOffer(offerId, merchandiseId);

        // Then
        assertThat(activeOffer).isEqualTo(getOffer(merchandiseId, merchantId, expiryDate, description, currencyCode, priceAmount, active));
    }

    @Test
    public void shouldNotReturnInactiveOffer() {
        // Given
        final UUID offerId = randomUUID();
        final UUID merchandiseId = randomUUID();
        final UUID merchantId = randomUUID();
        final String currencyCode = "GBP";
        final boolean active = false;
        final BigDecimal priceAmount = BigDecimal.TEN;
        final LocalDate expiryDate = LocalDate.now().plusDays(1);
        final String description = randomAlphabetic(10);
        final OfferDTO offerDTO = getOfferDTO(description, active, merchantId, merchandiseId, currencyCode, priceAmount, expiryDate);
        given(offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId)).willReturn(offerDTO);

        // When
        final com.beddoed.offers.model.Offer activeOffer = offerService.getActiveOffer(offerId, merchandiseId);

        // Then
        assertThat(activeOffer).isNull();
    }

    @Test
    public void shouldThrowOfferExpiredExceptionWhenOfferHasExpired() {
        // Given
        final UUID offerId = randomUUID();
        final UUID merchandiseId = randomUUID();
        final UUID merchantId = randomUUID();
        final String currencyCode = "GBP";
        final boolean active = true;
        final BigDecimal priceAmount = BigDecimal.TEN;
        final LocalDate expiryDate = LocalDate.now().minusDays(1);
        final String description = randomAlphabetic(10);
        final OfferDTO offerDTO = getOfferDTO(description, active, merchantId, merchandiseId, currencyCode, priceAmount, expiryDate);
        given(offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId)).willReturn(offerDTO);

        // Expect
        expectedException.expect(OfferExpiredException.class);
        expectedException.expectMessage(String.format("Offer with ID: %s has expired on date: %s", offerId, expiryDate));

        // When
        offerService.getActiveOffer(offerId, merchandiseId);
    }

    @Test
    public void shouldBeAbleToCancelOffer() {
        // Given
        final UUID offerId = randomUUID();
        final UUID merchandiseId = randomUUID();

        // When
        offerService.cancelOffer(offerId, merchandiseId);

        // Then
        verify(offerRepository).cancelOffer(offerId, merchandiseId);
    }

    private com.beddoed.offers.model.Offer getOffer(UUID merchandiseId, UUID merchantId, LocalDate expiryDate, String description, String currencyCode, BigDecimal priceAmount, Boolean active) {
        final Merchant merchant = merchantBuilder().merchantId(merchantId).build();
        final Merchandise product = MerchandiseBuilder
                .merchandiseBuilder()
                .merchandiseId(merchandiseId)
                .merchant(merchant)
                .buildProduct();
        final Price price = PriceBuilder.priceBuilder().currency(currencyCode).amount(priceAmount).build();
        return offerBuilder()
                .merchandise(product)
                .expiryDate(expiryDate)
                .description(description)
                .price(price)
                .active(active)
                .build();

    }

    private OfferDTO getOfferDTO(String description, boolean active, UUID merchantId, UUID merchandiseId, String currencyCode, BigDecimal amount, LocalDate expiryDate) {
        return new OfferDTO(description, new MerchandiseDTO(merchandiseId, MerchandiseType.PRODUCT, new MerchantDTO(merchantId)),
                currencyCode, amount, active, expiryDate);
    }

}