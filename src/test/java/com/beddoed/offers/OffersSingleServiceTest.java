package com.beddoed.offers;

import com.beddoed.Application;
import com.beddoed.offers.data.MerchandiseType;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferService;
import com.beddoed.offers.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static com.beddoed.offers.web.OfferRequestDataFactory.ACTIVE_OFFER_REQUEST;
import static com.beddoed.offers.web.OfferRequestDataFactory.toJson;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Application.class)
//@Sql(scripts = "classpath:data-load.sql")
public class OffersSingleServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OfferService offerService;

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

        final UUID offerId = UUID.randomUUID();
        given(offerService.createOffer(any(Offer.class))).willReturn(offerId);

        assertThat(jdbcUtils.countOffers(Optional.empty())).isEqualTo(0);

        // When
        final ResponseEntity<Void> exchange = restTemplate.exchange(requestEntity, Void.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getHeaders().getLocation().getPath()).isEqualTo(offerUri + "/" + offerId);

        assertThat(jdbcUtils.countOffers(Optional.of(offerId))).isEqualTo(1);
    }

    private void setupData() {
        jdbcUtils.insertCurrency("GBP");
        final UUID merchantId = UUID.randomUUID();
        jdbcUtils.insertMerchant(merchantId);
        jdbcUtils.insertMerchandise(merchantId, merchandiseId, MerchandiseType.PRODUCT);
    }
}

@Configuration
class TestConfig {

    @Bean
    public OfferService offerService() {
        return Mockito.mock(OfferService.class);
    }

    @Bean
    public MerchandiseService merchandiseService() {
        return Mockito.mock(MerchandiseService.class);
    }
}
