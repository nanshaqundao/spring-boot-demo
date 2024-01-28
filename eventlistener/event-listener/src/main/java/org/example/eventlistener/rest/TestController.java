package org.example.eventlistener.rest;

import jakarta.annotation.Resource;
import org.example.eventlistener.model.*;
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
    public void publishAddOrderEvent() {
        applicationContext.publishEvent(new OrderEvent(new Order("Order 1"), "add"));
    }

    @GetMapping("/publishAddEvent")
    public void publishEvent() {
        applicationContext.publishEvent(new BaseEvent<>(new Person("John"), "add"));
        applicationContext.publishEvent(new BaseEvent<>(new Order("John's Order"), "update"));
    }
}
