package com.beddoed.offers.service;

import com.beddoed.offers.data.MerchandiseRepository;
import com.beddoed.offers.model.Merchandise;
import com.beddoed.offers.model.Merchant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static com.beddoed.offers.builders.MerchandiseBuilder.merchandiseBuilder;
import static com.beddoed.offers.data.MerchandiseType.PRODUCT;
import static com.beddoed.offers.data.MerchandiseType.SERVICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MerchandiseServiceImplTest {

    @Mock
    private MerchandiseRepository merchandiseRepository;

    private MerchandiseService merchandiseService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        merchandiseService = new MerchandiseServiceImpl(merchandiseRepository);
    }

    @Test
    public void shouldFetchMerchandiseById() {
        // Given
        final UUID merchandiseId = UUID.randomUUID();
        final UUID merchantId = UUID.randomUUID();
        final Merchant merchant = new Merchant(merchantId);
        final Merchandise expected = merchandiseBuilder()
                .merchandiseId(merchandiseId)
                .merchant(merchant)
                .buildProduct();
        given(merchandiseRepository.getOne(merchandiseId)).willReturn(getMerchandiseDTO(merchandiseId, merchantId));

        // When
        final Merchandise merchandiseById = merchandiseService.getMerchandiseById(merchandiseId);

        // Then
        assertThat(merchandiseById).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionIfUnsupportedMerchandiseType() {
        // Given
        final UUID merchandiseId = UUID.randomUUID();
        final com.beddoed.offers.data.Merchant merchant = new com.beddoed.offers.data.Merchant(UUID.randomUUID());
        given(merchandiseRepository.getOne(merchandiseId)).willReturn(new com.beddoed.offers.data.Merchandise(merchandiseId, SERVICE, merchant));

        // Expect
        expectedException.expect(UnsupportedOperationException.class);
        expectedException.expectMessage("Merchandise type not supported");

        // When
        merchandiseService.getMerchandiseById(merchandiseId);
    }

    private com.beddoed.offers.data.Merchandise getMerchandiseDTO(UUID merchandiseId, UUID merchantId) {
        return new com.beddoed.offers.data.Merchandise(merchandiseId, PRODUCT, new com.beddoed.offers.data.Merchant(merchantId));
    }

}