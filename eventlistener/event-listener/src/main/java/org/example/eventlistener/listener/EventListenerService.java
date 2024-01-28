package org.example.eventlistener.listener;

import org.example.eventlistener.model.OrderEvent;
import org.example.eventlistener.model.PersonEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerService {
    @EventListener
    public void handlePersonEvent(PersonEvent personEvent) {
        System.out.println("Get PersonEvent: " + personEvent);
    }

    @EventListener
    public void handleOrderEvent(OrderEvent orderEvent) {
        System.out.println("Get OrderEvent: " + orderEvent);
    }
}
