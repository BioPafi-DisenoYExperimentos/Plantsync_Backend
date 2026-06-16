package com.plantsync.platform.iam.domain.exceptions;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {
  /**
   * Constructor for UserNotFoundException.
   *
   * @param email The email.
   */
  public UserNotFoundException(String email) {
    super(String.format("User with email '%s' not found.", email));
  }
}
