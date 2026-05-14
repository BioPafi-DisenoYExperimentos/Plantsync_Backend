package com.plantsync.platform.profiles.interfaces.rest.transform;

import com.plantsync.platform.profiles.domain.model.aggregates.Profile;
import com.plantsync.platform.profiles.interfaces.rest.resources.ProfileResource;

public final class ProfileResourceFromEntityAssembler {

    private ProfileResourceFromEntityAssembler() {
        // Utility class
    }

    public static ProfileResource toResourceFromEntity(Profile entity) {

        return new ProfileResource(
                entity.getId(),
                entity.getPersonName().name(),
                entity.getSubscriptionPlan().name(),
                entity.getUserId().value()
        );
    }
}
