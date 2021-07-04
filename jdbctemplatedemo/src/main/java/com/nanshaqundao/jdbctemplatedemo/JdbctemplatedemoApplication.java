package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class JdbctemplatedemoApplication implements CommandLineRunner {
    @Autowired
    private DataSourceManager dataSourceManager;

    public static void main(String[] args) {
        SpringApplication.run(JdbctemplatedemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Connection connection = dataSourceManager.getDataSource().getConnection();

        System.out.println("hi");

    }
}
