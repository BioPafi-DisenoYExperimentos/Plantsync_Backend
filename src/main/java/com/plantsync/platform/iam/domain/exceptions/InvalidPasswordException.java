package com.plantsync.platform.iam.domain.exceptions;

/**
 * Exception thrown when a password does not match the stored credentials.
 */
public class InvalidPasswordException extends RuntimeException {
  /**
   * Constructor for InvalidPasswordException.
   */
  public InvalidPasswordException() {
    super("Invalid password");
  }
}
