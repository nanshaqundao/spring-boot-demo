package org.example.eventlistener.listener;

import org.example.eventlistener.model.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerService {
//    @EventListener
//    public void handlePersonEvent(PersonEvent personEvent) {
//        System.out.println("Get PersonEvent: " + personEvent);
//    }
//
//    @EventListener
//    public void handleOrderEvent(OrderEvent orderEvent) {
//        System.out.println("Get OrderEvent: " + orderEvent);
//    }
//
//    @EventListener
//    public void handleEvent(BaseEvent<?> baseEvent) {
//        System.out.println("Get BaseEvent: " + baseEvent);
//        Object data = baseEvent.data();
//        if (data instanceof Person) {
//            System.out.println("Extracted PersonEvent: " + data);
//        } else if (data instanceof Order) {
//            System.out.println("Extracted OrderEvent: " + data);
//        }
//
//    }

    @EventListener
    public void handlePersonEvent(BaseEvent<Person> personEvent) {
        System.out.println("Get PersonEvent: " + personEvent);
    }

    @EventListener
    public void handleOrderEvent(BaseEvent<Order> orderEvent) {
        System.out.println("Get OrderEvent: " + orderEvent);
    }
}
