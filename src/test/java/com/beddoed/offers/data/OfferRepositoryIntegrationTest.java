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
import java.util.UUID;

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
        final UUID merchantId = UUID.randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = UUID.randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final Merchandise merchandise = new Merchandise(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final Offer offer = new Offer("test", merchandise, currencyCode, price, true);

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
        final UUID merchantId = UUID.randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = UUID.randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final Merchandise merchandise = new Merchandise(merchandiseId, product, merchant);
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP);
        final BigDecimal price2 = BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_UP);

        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        final Offer offer1 = new Offer("test", merchandise, currencyCode, price, true);
        final Offer offer2 = new Offer("test2", merchandise, currencyCode, price2, true);

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
}
