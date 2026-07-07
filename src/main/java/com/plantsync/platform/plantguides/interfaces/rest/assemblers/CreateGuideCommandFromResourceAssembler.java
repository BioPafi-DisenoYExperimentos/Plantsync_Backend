package com.plantsync.platform.plantguides.interfaces.rest.assemblers;

import com.plantsync.platform.plantguides.domain.model.commands.CreateGuideCommand;
import com.plantsync.platform.plantguides.interfaces.rest.resources.CreateGuideResource;

/**
 * Guide Command Assembler.
 * */
public class CreateGuideCommandFromResourceAssembler {

  private CreateGuideCommandFromResourceAssembler() {
    // Utility class
  }
  /**
   * CreateGuideCommand converted to resource.
   * */
  public static CreateGuideCommand toCommandFromResource(CreateGuideResource resource) {

    return new CreateGuideCommand(
        resource.title(),
        resource.name(),
        resource.description(),
        resource.topic(),
        resource.type(),
        resource.imageUrl());

  }
}