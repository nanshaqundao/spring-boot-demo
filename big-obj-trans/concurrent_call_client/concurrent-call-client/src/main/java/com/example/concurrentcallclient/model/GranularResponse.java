package com.example.concurrentcallclient.model;

public record GranularResponse(
    String name, String updatedData, boolean result, String failureMessage) {}
