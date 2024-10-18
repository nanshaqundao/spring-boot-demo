package com.example.datasourceservice.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  //  @ExceptionHandler(ResponseStatusException.class)
  //  public void handleResponseStatusException(ResponseStatusException ex) {
  //    throw ex; // Spring will automatically handle ResponseStatusException and return the correct
  //              // status
  //  }
}
