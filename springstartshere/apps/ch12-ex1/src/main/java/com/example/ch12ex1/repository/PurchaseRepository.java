package com.example.ch12ex1.repository;

import com.example.ch12ex1.model.Purchase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseRepository {

    private final JdbcTemplate jdbcTemplate;


    public PurchaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void storePurchase(Purchase purchase) {
        String sql = "INSERT INTO purchase VALUES(?, ?, ?)";

        jdbcTemplate.update(sql, purchase.getId(), purchase.getProduct(), purchase.getPrice());
    }

    public List<Purchase> findAllPurchases() {
        String sql = "SELECT * FROM purchase";
        RowMapper<Purchase> purchaseRowMapper = ((rs, rowNum) -> {
            Purchase rowObject = new Purchase();
            rowObject.setId(rs.getInt("id"));
            rowObject.setProduct(rs.getString("product"));
            rowObject.setPrice(rs.getBigDecimal("price"));
            return rowObject;
        });

        return jdbcTemplate.query(sql, purchaseRowMapper);
    }
}
