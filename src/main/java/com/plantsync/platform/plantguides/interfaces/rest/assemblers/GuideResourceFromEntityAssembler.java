package com.plantsync.platform.plantguides.interfaces.rest.assemblers;

import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.interfaces.rest.resources.GuideResource;

public final class GuideResourceFromEntityAssembler {

 private GuideResourceFromEntityAssembler() {
  // Utility class
 }

 public static GuideResource toResourceFromEntity(Guide entity) {
  return new GuideResource(
          entity.getId(),
          entity.getTitle(),
          entity.getName(),
          entity.getDescription(),
          entity.getTopic(),
          entity.getType(),
          entity.getImageUrl()
  );
 }
}