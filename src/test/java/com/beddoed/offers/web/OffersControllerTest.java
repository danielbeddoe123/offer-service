package com.beddoed.offers.web;

import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import com.beddoed.offers.model.Offer;
import com.beddoed.offers.model.Product;
import com.beddoed.offers.service.MerchandiseService;
import com.beddoed.offers.service.OfferService;
import com.beddoed.offers.web.resource.OfferResource;
import com.google.gson.GsonBuilder;
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
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.beddoed.offers.builders.MerchandiseBuilder.merchandiseBuilder;
import static com.beddoed.offers.builders.OfferBuilder.offerBuilder;
import static com.beddoed.offers.builders.PriceBuilder.priceBuilder;
import static com.beddoed.offers.utils.TestUtils.randomOneOf;
import static com.beddoed.offers.web.resource.OfferResourceDataFactory.toJson;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.Currency.getInstance;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class OffersControllerTest {

    private static final String CREATE_URI_TEMPLATE = "/merchandise/{merchandiseId}/offer";
    private static final String GET_URI_TEMPLATE = CREATE_URI_TEMPLATE + "/{offerId}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OfferService offerService;

    @MockBean
    private MerchandiseService merchandiseService;

    private UUID merchandiseId;

    @Before
    public void setup() {
         merchandiseId = randomUUID();
    }

    @Test
    public void testAllowOfferToBeCreated() throws Exception {
        // Given
        final UUID offerId = randomUUID();
        final String year = "2018";
        final String month = "03";
        final String dayOfMonth = "02";
        final String isoFormatExpiryDate = StringUtils.join(Arrays.array(year, month, dayOfMonth), "-");
        final String description = "Some description";
        final String currencyCode = "GBP";
        final BigDecimal amount = BigDecimal.valueOf(20.00).setScale(2, ROUND_HALF_UP);
        final boolean active = new Random().nextBoolean();
        final LocalDate expiryDate = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(dayOfMonth));
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final Product product = new Product(randomUUID(), merchant);

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
        final String jsonRequest = toJson(isoFormatExpiryDate, description, currencyCode, amount, active);

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
    public void shouldReturnUnprocessableEntityIfInvalidCurrencyCode() throws Exception {
        // Given
        final String invalidCurrency = randomOneOf("", "abcd");
        final List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldErrorResponse("currencyCode", "ValidCurrency", "Currency code is not valid"));
        final ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(fieldErrors);
        final String expectedApiErrorResponseJson = new GsonBuilder().create().toJson(expectedApiErrorResponse);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson("2018-03-02", "Something", invalidCurrency, BigDecimal.valueOf(0.5), false)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(expectedApiErrorResponseJson));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfExpiryDateIsMissing() throws Exception {
        // Given
        final List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldErrorResponse("expiryDate", "NotNull", "Expiry date cannot be null"));
        final ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(fieldErrors);
        final String expectedApiErrorResponseJson = new GsonBuilder().create().toJson(expectedApiErrorResponse);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(null, "Something", "GBP", BigDecimal.valueOf(0.5), false)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(expectedApiErrorResponseJson));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfDescriptionIsMissing() throws Exception {
        // Given
        final List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldErrorResponse("description", "NotEmpty", "Description cannot be empty"));
        final ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(fieldErrors);
        final String expectedApiErrorResponseJson = new GsonBuilder().create().toJson(expectedApiErrorResponse);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson("2018-03-02", "", "GBP", BigDecimal.valueOf(0.5), false)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(expectedApiErrorResponseJson));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfPriceAmountIsMissing() throws Exception {
        // Given
        final List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldErrorResponse("priceAmount", "NotNull", "Price amount cannot be null"));
        final ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(fieldErrors);
        final String expectedApiErrorResponseJson = new GsonBuilder().create().toJson(expectedApiErrorResponse);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson("2018-03-02", "Something", "GBP", null, false)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(expectedApiErrorResponseJson));
    }

    @Test
    public void shouldReturnUnprocessableEntityIfPriceAmountIsNotAPositiveNumber() throws Exception {
        // Given
        final List<FieldErrorResponse> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldErrorResponse("priceAmount", "DecimalMin", "Price amount must be a positive number"));
        final ApiErrorResponse expectedApiErrorResponse = new ApiErrorResponse(fieldErrors);
        final String expectedApiErrorResponseJson = new GsonBuilder().create().toJson(expectedApiErrorResponse);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson("2018-03-02", "Something", "GBP", BigDecimal.valueOf(-1.0), false)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(expectedApiErrorResponseJson));
    }

    @Test
    public void shouldCreateOfferIfPriceAmountIsZero() throws Exception {
        // Given
        final UUID offerId = randomUUID();
        given(offerService.createOffer(any(Offer.class))).willReturn(offerId);
        given(merchandiseService.getMerchandiseById(merchandiseId)).willReturn(mock(Merchandise.class));

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson("2018-03-02", "Something", "GBP", BigDecimal.valueOf(0), false)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, equalTo("/merchandise/" + merchandiseId + "/offer/" + offerId)));

    }

    @Test
    public void shouldReturnBadRequestIfMalformedJson() throws Exception {
        // Given

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnInternalServerErrorIfExceptionInService() throws Exception {
        // Given
        final String year = "2018";
        final String month = "03";
        final String dayOfMonth = "02";
        final String isoFormatExpiryDate = StringUtils.join(Arrays.array(year, month, dayOfMonth), "-");
        final String description = "Some description";
        final String currencyCode = "GBP";
        final BigDecimal amount = BigDecimal.valueOf(20.00).setScale(2, ROUND_HALF_UP);
        final boolean active = new Random().nextBoolean();
        final UUID merchantId = randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final Product product = new Product(randomUUID(), merchant);

        given(offerService.createOffer(any(Offer.class))).willThrow(new RuntimeException("Some exception!"));
        given(merchandiseService.getMerchandiseById(merchandiseId)).willReturn(product);
        final String jsonRequest = toJson(isoFormatExpiryDate, description, currencyCode, amount, active);

        // When / Then
        mvc.perform(put(CREATE_URI_TEMPLATE, merchandiseId)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldBeAbleToQueryOffers() throws Exception {
        // Given
        final UUID offerId = randomUUID();
        final Merchandise merchandise = merchandiseBuilder().merchandiseId(merchandiseId).buildProduct();
        final Offer offer = offerBuilder().merchandise(merchandise).build();
        given(offerService.getOffer(offerId, merchandiseId)).willReturn(offer);

        // When
        final MvcResult mvcResult = mvc.perform(get(GET_URI_TEMPLATE, merchandiseId, offerId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        final String json = mvcResult.getResponse().getContentAsString();
        final OfferResource offerResource = new GsonBuilder().create().fromJson(json, OfferResource.class);
        assertThat(offerResource.getDescription()).isEqualTo(offer.getDescription());
        assertThat(offerResource.getCurrencyCode()).isEqualTo(offer.getPrice().getCurrency().getCurrencyCode());
        assertThat(offerResource.getExpiryDate()).isEqualTo(offer.getExpiryDate().format(DateTimeFormatter.ISO_DATE));
        assertThat(offerResource.getPriceAmount()).isEqualTo(offer.getPrice().getAmount());
    }

    @Test
    public void shouldReturnNotFoundIfOfferDoesntExist() throws Exception {
        // Given

        // When / Then
        mvc.perform(get(GET_URI_TEMPLATE, merchandiseId, randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnHttpStatusOfGoneIfOfferHasExpired() throws Exception {
        // Given
        final UUID offerId = randomUUID();
        final Merchandise merchandise = merchandiseBuilder().merchandiseId(merchandiseId).buildProduct();
        final Offer offer = offerBuilder()
                .merchandise(merchandise)
                .expiryDate(LocalDate.now().minusDays(1))
                .build();
        given(offerService.getOffer(offerId, merchandiseId)).willReturn(offer);

        // When / Then
        mvc.perform(get(GET_URI_TEMPLATE, merchandiseId, offerId))
                .andExpect(status().isGone());
    }

    private class ApiErrorResponse {
        private List<FieldErrorResponse> fieldErrors;

        public ApiErrorResponse(List<FieldErrorResponse> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }

        public List<FieldErrorResponse> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(List<FieldErrorResponse> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }

    private class FieldErrorResponse {
        private String field;
        private String code;
        private String message;

        public FieldErrorResponse(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
