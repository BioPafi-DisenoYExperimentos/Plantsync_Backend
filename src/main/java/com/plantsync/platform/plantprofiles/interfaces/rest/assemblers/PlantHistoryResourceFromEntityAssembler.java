package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.PlantHistory;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.PlantHistoryResource;

/**
 * The type Plant history resource from entity assembler.
 */
public class PlantHistoryResourceFromEntityAssembler {
  private PlantHistoryResourceFromEntityAssembler() {
    // Utility class
  }
  /**
   * To resource from entity plant history resource.
   *
   * @param entity the entity
   * @return the plant history resource
   */
  public static PlantHistoryResource toResourceFromEntity(PlantHistory entity) {

    return new PlantHistoryResource(
        entity.getId(),
        entity.getPlantId().value(),
        entity.getType(),
        entity.getDate().toString(),
        entity.getTime().toString(),
        entity.getHumidity()

    );
  }
}
