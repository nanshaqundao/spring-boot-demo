package com.example.concurrentcallclient.exception;

public class ClientErrorException extends RuntimeException {
  private final int statusCode;

  public ClientErrorException(int statusCode) {
    super("Client error: " + statusCode);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
