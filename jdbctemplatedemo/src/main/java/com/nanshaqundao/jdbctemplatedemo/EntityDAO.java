package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EntityDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getCountOfEntity() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ENTITY", Integer.class);
    }
}
