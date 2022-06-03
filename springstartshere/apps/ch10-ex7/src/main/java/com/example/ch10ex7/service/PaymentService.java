package com.example.ch10ex7.service;


import com.example.ch10ex7.exception.NotEnoughMoneyException;
import com.example.ch10ex7.model.PaymentDetails;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public PaymentDetails processPayment(){
        throw new NotEnoughMoneyException();
    }
}
