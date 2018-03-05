package com.beddoed.offers.utils;

import com.beddoed.offers.data.MerchandiseType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class JdbcUtils {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertMerchandise(UUID merchantId, UUID merchandiseId, MerchandiseType product) {
        jdbcTemplate.update("insert into merchandise (merchandise_Id, merchandise_Type, merchant_merchant_Id) VALUES (?, ?, ?)", merchandiseId, product.toString(), merchantId);
    }

    public void insertCurrency(String currencyCode) {
        jdbcTemplate.update("insert into currency (currency_code) VALUES (?)", currencyCode);
    }

    public void insertMerchant(UUID merchantId) {
        jdbcTemplate.update("insert into merchant (merchant_Id) VALUES (?)", merchantId);
    }

    public long countOffers(Optional<UUID> offerId) {
        if (offerId.isPresent()) {
            return jdbcTemplate.queryForObject("select count(*) from offer where offer_Id = ?", Long.class, offerId.get());
        }
        return jdbcTemplate.queryForObject("select count(*) from offer", Long.class);
    }
}
