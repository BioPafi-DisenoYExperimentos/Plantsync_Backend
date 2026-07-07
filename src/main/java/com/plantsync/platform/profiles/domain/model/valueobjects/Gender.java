package com.plantsync.platform.profiles.domain.model.valueobjects;

/**
 * The enum Gender.
 */
public enum Gender {
  /**
   * Male gender.
   */
  MALE,
  /**
   * Female gender.
   */
  FEMALE,
  /**
   * Other gender.
   */
  OTHER,
  /**
   * Prefer not to say gender.
   */
  PREFER_NOT_TO_SAY;

  /**
   * From string gender.
   *
   * @param value the value
   * @return the gender
   */
  public static Gender fromString(String value) {
    try {
      return Gender.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Invalid gender: " + value);
    }
  }
}
