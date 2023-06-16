package com.example.demo.model;

import org.springframework.http.HttpStatusCode;

public class DummyWrapper {
  private HttpStatusCode statusCode;
  private String body;

  public DummyWrapper(HttpStatusCode statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  public DummyWrapper() {}

  public HttpStatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(HttpStatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
