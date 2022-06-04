package com.example.ch11paymentsresttemplate.controller;

import com.example.ch11paymentsresttemplate.model.Payment;
import com.example.ch11paymentsresttemplate.proxy.PaymentProxy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentProxy paymentProxy;

    public PaymentController(PaymentProxy paymentProxy) {
        this.paymentProxy = paymentProxy;
    }

    @PostMapping("/payment")
    public Payment createPayment(
            @RequestBody Payment payment
    ){
        return paymentProxy.createPayment(payment);
    }
}
