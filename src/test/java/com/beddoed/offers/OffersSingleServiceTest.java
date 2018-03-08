package com.beddoed.offers;

import com.beddoed.Application;
import com.beddoed.offers.data.MerchandiseType;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.utils.JdbcUtils;
import com.beddoed.offers.web.resource.OfferResource;
import org.junit.After;
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

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @After
    public void clearData() {
        jdbcUtils.clearOffers();
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

    @Test
    public void shouldBeAbleToFetchActiveOffer() throws InterruptedException {
        // Given
        final UUID offerId = UUID.randomUUID();
        final String offerUri = "/merchandise/" + merchandiseId + "/offer/" + offerId;

        final LocalDate expiryDate = LocalDate.now().plusDays(10);
        final String description = "Active OfferDTO";
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.valueOf(100.00).setScale(2, BigDecimal.ROUND_HALF_UP);
        final boolean active = true;

        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);
        assertThat(jdbcUtils.countOffers()).isEqualTo(1);

        final OfferResource expectedResource = new OfferResource(expiryDate.format(DateTimeFormatter.ISO_DATE), description, currencyCode, price, active);
        final RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(offerUri)).build();

        // When
        final ResponseEntity<OfferResource> exchange = restTemplate.exchange(requestEntity, OfferResource.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jdbcUtils.countOffers()).isEqualTo(1);

        final OfferResource actualResource = exchange.getBody();
        assertThat(actualResource).isEqualTo(expectedResource);
    }

    @Test
    public void shouldReturnHttpGoneStatusWhenOfferHasExpired() throws InterruptedException {
        // Given
        final UUID offerId = UUID.randomUUID();
        final String offerUri = "/merchandise/" + merchandiseId + "/offer/" + offerId;

        final LocalDate expiryDate = LocalDate.now().minusDays(10);
        final String description = "Active OfferDTO";
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.valueOf(100.00).setScale(2, BigDecimal.ROUND_HALF_UP);
        final boolean active = true;

        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);
        assertThat(jdbcUtils.countOffers()).isEqualTo(1);

        final RequestEntity<Void> requestEntity = RequestEntity.get(URI.create(offerUri)).build();

        // When
        final ResponseEntity<Void> exchange = restTemplate.exchange(requestEntity, Void.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.GONE);
    }

    @Test
    public void shouldBeAbleToCancelOffer() throws InterruptedException {
        // Given
        final UUID offerId = UUID.randomUUID();
        final String offerUri = "/merchandise/" + merchandiseId + "/offer/" + offerId;

        final LocalDate expiryDate = LocalDate.now().plusDays(10);
        final String description = "Active OfferDTO";
        final String currencyCode = "GBP";
        final BigDecimal price = BigDecimal.valueOf(100.00).setScale(2, BigDecimal.ROUND_HALF_UP);
        final boolean active = true;

        jdbcUtils.insertOffer(offerId, description, merchandiseId, currencyCode, price, active, expiryDate);
        assertThat(jdbcUtils.countOffers()).isEqualTo(1);

        final RequestEntity<Void> requestEntity = RequestEntity.delete(URI.create(offerUri)).build();

        // When
        final ResponseEntity<OfferResource> exchange = restTemplate.exchange(requestEntity, OfferResource.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jdbcUtils.isOfferActive(offerId)).isFalse();
    }

    private void setupData() {
        final UUID merchantId = UUID.randomUUID();
        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, MerchandiseType.PRODUCT);
    }
}