package org.example.eventlistener.model;

public record OrderEvent(Order order, String addOrUpdate) {
}
