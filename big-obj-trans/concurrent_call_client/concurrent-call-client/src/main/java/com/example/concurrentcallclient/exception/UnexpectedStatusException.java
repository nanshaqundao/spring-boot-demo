package com.example.concurrentcallclient.exception;

public class UnexpectedStatusException extends RuntimeException {
  private final int statusCode;

  public UnexpectedStatusException(int statusCode) {
    super("Unexpected status code: " + statusCode);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
