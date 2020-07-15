package com.ecommerce.customerfavorites.exception;

import org.springframework.http.HttpStatus;

/** @author Rajni Kanth Tupakula */
public class BaseApiException extends RuntimeException {
  private final HttpStatus status;

  public BaseApiException() {
    super("No message provided for the error. Contact the developer with the error code and uri");
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public BaseApiException(String message) {
    super(message);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public HttpStatus getStatus() {
    return this.status;
  }
}
