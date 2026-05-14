package com.plantsync.platform.iam.interfaces.rest.transform;

import com.plantsync.platform.iam.domain.model.commands.SignInCommand;
import com.plantsync.platform.iam.interfaces.rest.resources.SignInResource;

public final class SignInCommandFromResourceAssembler {

    private SignInCommandFromResourceAssembler() {
    }

    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(
                signInResource.email(),
                signInResource.password()
        );
    }
}