package com.plantsync.platform.profiles.interfaces.rest.transform;

import com.plantsync.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.plantsync.platform.profiles.domain.model.valueobjects.PersonName;
import com.plantsync.platform.profiles.domain.model.valueobjects.SubscriptionPlan;
import com.plantsync.platform.profiles.domain.model.valueobjects.UserId;
import com.plantsync.platform.profiles.interfaces.rest.resources.CreateProfileResource;

public final class CreateProfileCommandFromResourceAssembler {

    private CreateProfileCommandFromResourceAssembler() {
        // Utility class
    }

    public static CreateProfileCommand toCommandFromResource(
            CreateProfileResource resource
    ) {

        return new CreateProfileCommand(
                new PersonName(resource.personName()),
                SubscriptionPlan.valueOf(
                        resource.subscriptionPlan().toUpperCase()
                ),
                new UserId(resource.userId())
        );
    }
}