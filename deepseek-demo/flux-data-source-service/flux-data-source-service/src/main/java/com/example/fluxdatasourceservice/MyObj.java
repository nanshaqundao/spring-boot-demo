package com.example.fluxdatasourceservice;

public record MyObj(
        long id,
        String content
) {
    @Override
    public String toString() {
        return "MyObj{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}