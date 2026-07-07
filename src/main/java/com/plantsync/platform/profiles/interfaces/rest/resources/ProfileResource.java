package com.plantsync.platform.profiles.interfaces.rest.resources;

/**
 * Resource representation of a profile.
 *
 * @param id               The ID of the profile.
 * @param personName       The name of the person.
 * @param subscriptionPlan The subscription plan.
 * @param userId           The ID of the associated user.
 * @param age              The age of the person.
 * @param gender           The gender of the person.
 */
public record ProfileResource(
    Long id,
    String personName,
    String subscriptionPlan,
    Long userId,
    Integer age,
    String gender) {
}
