package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Product;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.Currency.getInstance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OffersController.class)
public class OffersControllerTest {

    private static final String CREATE_URI_TEMPLATE = "/merchandise/{merchandiseId}/offer";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OfferService offerService;

    @MockBean
    private MerchandiseService merchandiseService;

    private UUID merchandiseId;

    @Before
    public void setup() {
         merchandiseId = UUID.randomUUID();
    }

    @Test
    public void testAllowOfferToBeCreated() throws Exception {
        // Given
        final UUID offerId = UUID.randomUUID();
        final String year = "2018";
        final String month = "03";
        final String dayOfMonth = "02";
        final String isoFormatExpiryDate = StringUtils.join(Arrays.array(year, month, dayOfMonth), "-");
        final String description = "Some description";
        final String currencyCode = "GBP";
        final BigDecimal amount = BigDecimal.valueOf(20.00).setScale(2, ROUND_HALF_UP);
        final boolean active = new Random().nextBoolean();
        final LocalDate expiryDate = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(dayOfMonth));
        final UUID merchantId = UUID.randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final Product product = new Product(merchant);

        final Offer expectedOffer = offerBuilder()
                .merchandise(product)
                .expiryDate(expiryDate)
                .active(active)
                .price(priceBuilder()
                        .currency(getInstance(currencyCode))
                        .amount(amount)
                        .build())
                .description(description)
                .build();

        given(offerService.createOffer(expectedOffer)).willReturn(offerId);
        given(merchandiseService.getMerchandiseById(merchandiseId)).willReturn(product);
        final String jsonRequest = getJsonRequest(isoFormatExpiryDate, description, currencyCode, amount, active);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, equalTo("/merchandise/" + merchandiseId + "/offer/" + offerId)));
    }

    @Test
    public void shouldNotAllowOtherHttpMethods() throws Exception {
        // Given

        // When / Then
        mvc.perform(delete(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldNotAllowOtherMediaTypes() throws Exception {
        // Given

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfRequestNotValid() throws Exception {
        // Given

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonRequest("2018-03-02", "", null, BigDecimal.valueOf(0.5), false)))
                .andExpect(status().isUnprocessableEntity());
    }

    private String getJsonRequest(String expiryDate, String description, String currencyCode, BigDecimal amount, boolean active) {
        return "{\n" +
                "  \"expiryDate\": \"" + expiryDate + "\",\n" +
                "  \"description\": \"" + description + "\",\n" +
                "  \"currencyCode\": \"" + currencyCode + "\",\n" +
                "  \"priceAmount\": \"" + amount + "\",\n" +
                "  \"active\": " + active +"\n" +
                "}";
    }
}
