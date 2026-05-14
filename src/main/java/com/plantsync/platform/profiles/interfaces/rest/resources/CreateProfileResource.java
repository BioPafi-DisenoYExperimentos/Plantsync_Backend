package com.plantsync.platform.profiles.interfaces.rest.resources;

public record CreateProfileResource(

        String personName,
        String subscriptionPlan,
        Long userId

) {
}