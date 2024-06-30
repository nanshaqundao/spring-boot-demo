package com.example.concurrentcallclient.exception;

public class QueuePublishingException extends RuntimeException {
  public QueuePublishingException(String message) {
    super(message);
  }

  public QueuePublishingException(String message, Throwable cause) {
    super(message, cause);
  }
}
