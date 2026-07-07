package com.plantsync.platform.plantguides.interfaces.rest.assemblers;

import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.interfaces.rest.resources.GuideResource;

/**
 * Assembler class to convert {@link Guide} entity to {@link GuideResource}.
 */

public class GuideResourceFromEntityAssembler {

  private GuideResourceFromEntityAssembler() {
    // Utility class
  }
  /**
   * Converts a {@link Guide} entity to a {@link GuideResource}.
   *
   * @param entity The guide entity.
   * @return The guide resource.
   */
  public static GuideResource toResourceFromEntity(Guide entity) {
    return new GuideResource(
        entity.getId(),
        entity.getTitle(),
        entity.getName(),
        entity.getDescription(),
        entity.getTopic(),
        entity.getType(),
        entity.getImageUrl());
  }

}
