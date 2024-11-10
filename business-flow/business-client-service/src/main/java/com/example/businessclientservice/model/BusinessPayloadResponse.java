package com.example.businessclientservice.model;

public class BusinessPayloadResponse {
  private final String jsonPayload;

  public BusinessPayloadResponse(String jsonPayload) {
    this.jsonPayload = jsonPayload;
  }

  public String getJsonPayload() {
    return jsonPayload;
  }
}
