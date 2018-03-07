package com.beddoed.offers.data;

import com.beddoed.offers.utils.JdbcUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static com.beddoed.offers.utils.TestUtils.randomBoolean;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OfferRepositoryIntegrationTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcUtils jdbcUtils;

    @Before
    public void setup() {
        jdbcUtils = new JdbcUtils(jdbcTemplate);
    }

    @Test
    public void shouldSaveAndFindOfferInDatabase() {
        // Given
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final Merchandise merchandise = new Merchandise(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final Offer offer = new Offer("test", merchandise, currencyCode, price, true, expiryDate);

        // When
        Offer savedOffer = offerRepository.save(offer);

        // Then
        assertThat(savedOffer.getOfferId()).isNotNull();
        Offer foundOffer = offerRepository.findOne(savedOffer.getOfferId());
        assertThat(foundOffer).isNotNull();
        assertThat(savedOffer).isEqualTo(foundOffer);
    }

    @Test
    public void shouldAllowMultipleOffersToBeSavedAgainstMerchandise() {
        // Given
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final Merchandise merchandise = new Merchandise(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final BigDecimal price2 = BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final Offer offer1 = new Offer("test", merchandise, currencyCode, price, true, expiryDate);
        final Offer offer2 = new Offer("test2", merchandise, currencyCode, price2, true, expiryDate);

        // When
        Offer savedOffer1 = offerRepository.save(offer1);
        Offer savedOffer2 = offerRepository.save(offer2);

        // Then
        assertThat(offerRepository.count()).isEqualTo(2);

        assertThat(savedOffer1.getOfferId()).isNotNull();
        Offer foundOffer1 = offerRepository.findOne(savedOffer1.getOfferId());
        assertThat(foundOffer1).isNotNull();
        assertThat(savedOffer1).isEqualTo(foundOffer1);

        assertThat(savedOffer2.getOfferId()).isNotNull();
        Offer foundOffer2 = offerRepository.findOne(savedOffer2.getOfferId());
        assertThat(foundOffer2).isNotNull();
        assertThat(savedOffer2).isEqualTo(foundOffer2);
    }

    @Test
    public void shouldFindByOfferAndMerchandiseId() {
        // Given
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final Merchandise merchandise = new Merchandise(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();
        final boolean active = randomBoolean();
        final String description = randomAlphabetic(10);
        final UUID offerId = randomUUID();
        final Offer expected = new Offer(description, merchandise, currencyCode, price, active, expiryDate);
        expected.setOfferId(offerId);

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);
        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);

        // When
        final Offer byOfferIdAndMerchandiseId = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId);

        // Then
        assertThat(byOfferIdAndMerchandiseId).isEqualTo(expected);
    }

    @Test
    public void shouldNotFindOfferIfOfferIsNotForMerchandiseId() {
        // Given
        final UUID merchantId = randomUUID();
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();
        final boolean active = randomBoolean();
        final String description = randomAlphabetic(10);
        final UUID offerId = randomUUID();
        final UUID otherMerchandiseId = randomUUID();

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);
        jdbcUtils.insertMerchandise(merchantId, otherMerchandiseId, product);
        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);

        // When
        final Offer byOfferIdAndMerchandiseId = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, otherMerchandiseId);

        // Then
        assertThat(byOfferIdAndMerchandiseId).isEqualTo(null);
    }
}
