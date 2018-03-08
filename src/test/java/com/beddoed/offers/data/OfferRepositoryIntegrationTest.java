package com.beddoed.offers.data;

import com.beddoed.offers.utils.JdbcUtils;
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
        jdbcUtils.clearOffers();
    }

    @Test
    public void shouldSaveAndFindOfferInDatabase() {
        // Given
        final UUID merchantId = randomUUID();
        final MerchantDTO merchant = new MerchantDTO(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final OfferDTO offerDTO = new OfferDTO("test", merchandiseDTO, currencyCode, price, true, expiryDate);

        // When
        OfferDTO savedOfferDTO = offerRepository.save(offerDTO);

        // Then
        assertThat(savedOfferDTO.getOfferId()).isNotNull();
        OfferDTO foundOffer = offerRepository.findOne(savedOfferDTO.getOfferId());
        assertThat(foundOffer).isNotNull();
        assertThat(savedOfferDTO).isEqualTo(foundOffer);
    }

    @Test
    public void shouldAllowMultipleOffersToBeSavedAgainstMerchandise() {
        // Given
        final UUID merchantId = randomUUID();
        final MerchantDTO merchant = new MerchantDTO(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final BigDecimal price2 = BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final OfferDTO offerDTO1 = new OfferDTO("test", merchandiseDTO, currencyCode, price, true, expiryDate);
        final OfferDTO offerDTO2 = new OfferDTO("test2", merchandiseDTO, currencyCode, price2, true, expiryDate);

        // When
        OfferDTO savedOfferDTO1 = offerRepository.save(offerDTO1);
        OfferDTO savedOfferDTO2 = offerRepository.save(offerDTO2);

        // Then
        assertThat(offerRepository.count()).isEqualTo(2);

        assertThat(savedOfferDTO1.getOfferId()).isNotNull();
        OfferDTO foundOffer1 = offerRepository.findOne(savedOfferDTO1.getOfferId());
        assertThat(foundOffer1).isNotNull();
        assertThat(savedOfferDTO1).isEqualTo(foundOffer1);

        assertThat(savedOfferDTO2.getOfferId()).isNotNull();
        OfferDTO foundOffer2 = offerRepository.findOne(savedOfferDTO2.getOfferId());
        assertThat(foundOffer2).isNotNull();
        assertThat(savedOfferDTO2).isEqualTo(foundOffer2);
    }

    @Test
    public void shouldFindByOfferAndMerchandiseId() {
        // Given
        final UUID merchantId = randomUUID();
        final MerchantDTO merchant = new MerchantDTO(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();
        final boolean active = randomBoolean();
        final String description = randomAlphabetic(10);
        final UUID offerId = randomUUID();
        final OfferDTO expected = new OfferDTO(description, merchandiseDTO, currencyCode, price, active, expiryDate);
        expected.setOfferId(offerId);

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);
        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);

        // When
        final OfferDTO byOfferIdAndMerchandiseId = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, merchandiseId);

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
        final OfferDTO byOfferIdAndMerchandiseId = offerRepository.findByOfferIdAndMerchandise_MerchandiseId(offerId, otherMerchandiseId);

        // Then
        assertThat(byOfferIdAndMerchandiseId).isEqualTo(null);
    }

    @Test
    public void shouldUpdateOfferToCanncelled() {
        // Given
        final UUID merchantId = randomUUID();
        final MerchantDTO merchant = new MerchantDTO(merchantId);
        final UUID merchandiseId = randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final MerchandiseDTO merchandiseDTO = new MerchandiseDTO(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final LocalDate expiryDate = LocalDate.now();
        final boolean active = true;
        final String description = randomAlphabetic(10);
        final UUID offerId = randomUUID();
        final OfferDTO expected = new OfferDTO(description, merchandiseDTO, currencyCode, price, active, expiryDate);
        expected.setOfferId(offerId);

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);
        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);
        assertThat(jdbcUtils.isOfferActive(offerId)).isEqualTo(true);

        // When
        final int numOffersCancelled = offerRepository.cancelOffer(offerId, merchandiseId);

        // Then
        assertThat(numOffersCancelled).isEqualTo(1);
        assertThat(jdbcUtils.isOfferActive(offerId)).isEqualTo(false);
    }
}
