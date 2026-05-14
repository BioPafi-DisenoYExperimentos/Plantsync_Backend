package com.plantsync.platform.iam.interfaces.rest.transform;

import com.plantsync.platform.iam.domain.model.commands.UpdateUserCommand;
import com.plantsync.platform.iam.interfaces.rest.resources.UpdateUserResource;

public final class UpdateUserCommandFromResourceAssembler {

    private UpdateUserCommandFromResourceAssembler() {
    }

    public static UpdateUserCommand toCommandFromResource(Long id, UpdateUserResource resource) {
        return new UpdateUserCommand(id, resource.email());
    }
}