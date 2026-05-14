package com.plantsync.platform.plantguides.interfaces.rest.assemblers;

import com.plantsync.platform.plantguides.domain.model.commands.CreateGuideCommand;
import com.plantsync.platform.plantguides.interfaces.rest.resources.CreateGuideResource;

public final class CreateGuideCommandFromResourceAssembler {

    private CreateGuideCommandFromResourceAssembler() {
    }

    public static CreateGuideCommand toCommandFromResource(CreateGuideResource resource) {

        return new CreateGuideCommand(
                resource.title(),
                resource.name(),
                resource.description(),
                resource.topic(),
                resource.type(),
                resource.imageUrl()
        );
    }
}