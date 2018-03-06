package com.beddoed.offers;

import com.beddoed.Application;
import com.beddoed.offers.data.MerchandiseType;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static com.beddoed.offers.web.resource.OfferResourceDataFactory.ACTIVE_OFFER_REQUEST;
import static com.beddoed.offers.web.resource.OfferResourceDataFactory.toJson;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Application.class)
public class OffersSingleServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MerchandiseService merchandiseService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcUtils jdbcUtils;

    private UUID merchandiseId;

    @Before
    public void setup() {
        jdbcUtils = new JdbcUtils(jdbcTemplate);
        merchandiseId = UUID.randomUUID();
        setupData();
    }

    @Test
    public void shouldBeAbleToCallOfferApiAndCreateAnOffer() throws InterruptedException {
        // Given
        final String offerUri = "/merchandise/" + merchandiseId + "/offer";
        final String offerRequestJson = toJson(ACTIVE_OFFER_REQUEST);

        final RequestEntity<String> requestEntity = RequestEntity.put(URI.create(offerUri))
                .contentType(MediaType.APPLICATION_JSON)
                .body(offerRequestJson);
        assertThat(jdbcUtils.countOffers()).isEqualTo(0);

        // When
        final ResponseEntity<Void> exchange = restTemplate.exchange(requestEntity, Void.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(jdbcUtils.countOffers()).isEqualTo(1);
        final UUID createdOfferId = jdbcUtils.getOfferId();

        assertThat(exchange.getHeaders().getLocation().getPath()).isEqualTo(offerUri + "/" + createdOfferId);

    }

    private void setupData() {
        final UUID merchantId = UUID.randomUUID();
        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, MerchandiseType.PRODUCT);
    }
}