package com.example.ch11paymentsopenfeign.model;


import lombok.Data;

@Data
public class Payment {
    private String id;
    private Double amount;
}
