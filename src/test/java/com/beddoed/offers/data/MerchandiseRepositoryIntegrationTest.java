package com.beddoed.offers.data;

import com.beddoed.offers.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MerchandiseRepositoryIntegrationTest {

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcUtils jdbcUtils;

    @Before
    public void setup() {
        jdbcUtils = new JdbcUtils(jdbcTemplate);
    }

    @Test
    public void shouldFindMerchandiseInDatabase() {
        // Given
        final UUID merchantId = UUID.randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final UUID merchandiseId = UUID.randomUUID();
        final MerchandiseType product = MerchandiseType.PRODUCT;
        final String currencyCode = "GBP";
        final Merchandise expectedMerchandise = new Merchandise(merchandiseId, product, merchant);

        jdbcUtils.insertCurrency(currencyCode);
        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, product);

        // When
        Merchandise savedMerchandise = merchandiseRepository.findOne(merchandiseId);

        // Then
        assertThat(savedMerchandise).isNotNull();
        assertThat(savedMerchandise).isEqualTo(expectedMerchandise);
    }
}
