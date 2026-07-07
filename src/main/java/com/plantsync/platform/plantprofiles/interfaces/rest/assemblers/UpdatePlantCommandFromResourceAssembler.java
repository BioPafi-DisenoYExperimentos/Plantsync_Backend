package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.commands.UpdatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.UpdatePlantResource;
import java.time.LocalDate;

/**
 * The type Update plant command from resource assembler.
 */
public class UpdatePlantCommandFromResourceAssembler {
  private UpdatePlantCommandFromResourceAssembler() {
    // Utility class
  }
  /**
   * To command from resource update plant command.
   *
   * @param plantId  the plant id
   * @param resource the resource
   * @return the update plant command
   */
  public static UpdatePlantCommand toCommandFromResource(
      Long plantId, UpdatePlantResource resource) {
    return new UpdatePlantCommand(
        plantId,
        new PlantName(resource.name()),
        resource.species(),
        LocalDate.parse(resource.acquisitionDate()),
        HumidityLevel.valueOf(resource.humidity().toUpperCase()),
        LocalDate.parse(resource.nextWateringDate()),
        resource.imageUrl(),
        resource.notificationsEnabled(),
        new ProfileId(resource.profileId())


    );
  }
}