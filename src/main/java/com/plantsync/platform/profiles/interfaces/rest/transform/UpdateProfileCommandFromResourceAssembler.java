package com.plantsync.platform.profiles.interfaces.rest.transform;

import com.plantsync.platform.profiles.domain.model.commands.UpdateProfileCommand;
import com.plantsync.platform.profiles.interfaces.rest.resources.UpdateProfileResource;

public final class UpdateProfileCommandFromResourceAssembler {

    private UpdateProfileCommandFromResourceAssembler() {
        // Utility class
    }

    public static UpdateProfileCommand toCommandFromResource(
            Long id,
            UpdateProfileResource resource
    ) {

        return new UpdateProfileCommand(
                id,
                resource.personName(),
                resource.subscriptionPlan()
        );
    }
}