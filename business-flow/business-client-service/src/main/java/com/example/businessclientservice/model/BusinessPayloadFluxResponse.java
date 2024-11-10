package com.example.businessclientservice.model;

import java.util.List;

public class BusinessPayloadFluxResponse {
  private final int count;
  private final List<String> jsonPayloads;

  public BusinessPayloadFluxResponse(int count, List<String> jsonPayloads) {
    this.count = count;
    this.jsonPayloads = jsonPayloads;
  }

  public int getCount() {
    return count;
  }

  public List<String> getJsonPayloads() {
    return jsonPayloads;
  }
}
