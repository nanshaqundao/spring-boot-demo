package com.nanshaqundao.jdbctemplatedemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JdbctemplatedemoApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityDAO entityDAO;

    @Test
    void contextLoads() {
        int number = entityDAO.getCountOfEntity();
        assertEquals(4, number);
    }

}
