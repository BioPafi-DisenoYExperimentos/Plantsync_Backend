package com.plantsync.platform.profiles.interfaces.rest.resources;

/**
 * The type Create profile resource.
 */
public record CreateProfileResource(

    String personName,
    String subscriptionPlan,
    Long userId

) {

}
