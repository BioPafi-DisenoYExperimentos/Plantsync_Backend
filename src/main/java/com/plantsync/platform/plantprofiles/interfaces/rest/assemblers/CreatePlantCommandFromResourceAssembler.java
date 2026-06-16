package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.CreatePlantResource;
import java.time.LocalDate;

/**
 * Create Plant Assembler.
 * */
public class CreatePlantCommandFromResourceAssembler {
  private CreatePlantCommandFromResourceAssembler() {
    // Utility class
  }
  /**
   * Create plant command to resource.
   * */
  public static CreatePlantCommand toCommandFromResource(CreatePlantResource resource) {

    return new CreatePlantCommand(
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