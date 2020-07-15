package com.ecommerce.customerfavorites.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rajni Kanth Tupakula
 */
public class NoDataFoundException extends RuntimeException {

  private final HttpStatus status;

  public NoDataFoundException() {
    super("No message provided for the error. Contact the developer with the error code and uri");
    this.status = HttpStatus.NOT_FOUND;
  }

  public NoDataFoundException(String message) {
    super(message);
    this.status = HttpStatus.NOT_FOUND;
  }

  public HttpStatus getStatus() {
    return this.status;
  }
}
