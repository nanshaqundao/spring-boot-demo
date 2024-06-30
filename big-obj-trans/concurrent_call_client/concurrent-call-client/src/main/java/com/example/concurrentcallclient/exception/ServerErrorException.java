package com.example.concurrentcallclient.exception;

public class ServerErrorException extends RuntimeException {
  private final int statusCode;

  public ServerErrorException(int statusCode) {
    super("Server error: " + statusCode);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
