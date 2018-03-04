package com.beddoed.offers;

import com.beddoed.Application;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.UUID;

import static com.beddoed.offers.web.OfferRequestDataFactory.ACTIVE_OFFER_REQUEST;
import static com.beddoed.offers.web.OfferRequestDataFactory.toJson;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Application.class)
public class OffersSingleServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private OfferService offerService;

    @Test
    public void shouldBeAbleToCallOfferApiAndCreateAnOffer() throws InterruptedException {
        // Given
        final String offerUri = "/merchandise/" + UUID.randomUUID() + "/offer";
        final String offerRequestJson = toJson(ACTIVE_OFFER_REQUEST);

        final RequestEntity<String> requestEntity = RequestEntity.put(URI.create(offerUri))
                .contentType(MediaType.APPLICATION_JSON)
                .body(offerRequestJson);

        final UUID offerId = UUID.randomUUID();
        given(offerService.createOffer(any(Offer.class))).willReturn(offerId);

        // When
        final ResponseEntity<Void> exchange = restTemplate.exchange(requestEntity, Void.class);

        // Then
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(exchange.getHeaders().getLocation().getPath()).isEqualTo(offerUri + "/" + offerId);
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
