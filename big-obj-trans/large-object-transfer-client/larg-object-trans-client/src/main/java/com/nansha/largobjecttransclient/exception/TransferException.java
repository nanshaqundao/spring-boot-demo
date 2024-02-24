package com.nansha.largobjecttransclient.exception;

public class TransferException extends RuntimeException {
  public TransferException(String message) {
    super(message);
  }

  public TransferException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransferException(Throwable cause) {
    super(cause);
  }
}
