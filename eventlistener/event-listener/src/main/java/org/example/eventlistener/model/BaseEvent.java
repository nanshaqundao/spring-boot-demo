package org.example.eventlistener.model;

public record BaseEvent<T>(T data, String addOrUpdate) {
}
