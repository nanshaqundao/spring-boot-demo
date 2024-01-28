package org.example.eventlistener.rest;

import jakarta.annotation.Resource;
import org.example.eventlistener.model.Order;
import org.example.eventlistener.model.OrderEvent;
import org.example.eventlistener.model.Person;
import org.example.eventlistener.model.PersonEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Resource
    private ApplicationContext applicationContext;
    ;

    @GetMapping("/publishAddPersonEvent")
    public void publishAddPersonEvent() {
        applicationContext.publishEvent(new PersonEvent(new Person("John"), "add"));
    }

    @GetMapping("/publishAddOrderEvent")
    public void publishAddOrderEventt sta() {
        applicationContext.publishEvent(new OrderEvent(new Order("Order 1"), "add"));
    }
}
