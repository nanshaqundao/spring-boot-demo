package org.example.eventlistener.rest;

import jakarta.annotation.Resource;
import org.example.eventlistener.model.BaseEvent;
import org.example.eventlistener.model.Order;
import org.example.eventlistener.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewTestController {

    @Resource
    private ApplicationContext applicationContext;

    @GetMapping("/publishNewAddEvent")
    public void publishEvent() {
        applicationContext.publishEvent(new BaseEvent<>(new Person("John"), "add"));
        applicationContext.publishEvent(new BaseEvent<>(new Order("John's Order"), "update"));
    }
}
