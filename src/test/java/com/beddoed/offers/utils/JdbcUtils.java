package com.beddoed.offers.utils;

import com.beddoed.offers.data.MerchandiseType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void insertMerchant(UUID merchantId) {
        jdbcTemplate.update("insert into merchant (merchant_Id) VALUES (?)", merchantId);
    }

    public long countOffers() {
        return jdbcTemplate.queryForObject("select count(*) from offer", Long.class);
    }

    public long countOffers(UUID offerId) {
        return jdbcTemplate.queryForObject("select count(*) from offer where offer_Id = ?", Long.class, offerId);
    }

    public UUID getOfferId() {
        return jdbcTemplate.queryForObject("select offer_Id from offer", (resultSet, i) -> {
            final byte[] bytes = resultSet.getBytes("offer_Id");
            return getGuidFromByteArray(bytes);
        });
    }

    private static UUID getGuidFromByteArray(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

}
