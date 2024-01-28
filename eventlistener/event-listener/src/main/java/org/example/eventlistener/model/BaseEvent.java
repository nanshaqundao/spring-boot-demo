package org.example.eventlistener.model;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public record BaseEvent<T>(T data, String addOrUpdate) implements ResolvableTypeProvider {
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(data));
    }
}
