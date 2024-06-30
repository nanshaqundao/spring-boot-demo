package com.example.concurrentcallclient.exception;

public class CustomRetryExhaustedException extends RuntimeException {
  public CustomRetryExhaustedException(String message, Throwable cause) {
    super(message, cause);
  }
}
