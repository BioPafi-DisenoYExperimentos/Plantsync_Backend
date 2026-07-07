package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.Plant;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.PlantResource;

/**
 * The type Plant resource from entity assembler.
 */
public class PlantResourceFromEntityAssembler {
  private PlantResourceFromEntityAssembler() {
    // Utility class
  }
  /**
   * To resource from entity plant resource.
   *
   * @param entity the entity
   * @return the plant resource
   */
  public static PlantResource toResourceFromEntity(Plant entity) {

    return new PlantResource(
        entity.getId(),
        entity.getName().value(),
        entity.getSpecies(),
        entity.getAcquisitionDate().toString(),
        entity.getHumidity().name(),
        entity.getNextWateringDate().toString(),
        entity.getImageUrl(),
        entity.getNotificationsEnabled(),
        entity.getProfileId().value()
    );
  }
}
