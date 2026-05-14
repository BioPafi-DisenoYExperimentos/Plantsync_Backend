package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.commands.UpdatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.UpdatePlantResource;

import java.time.LocalDate;

public final class UpdatePlantCommandFromResourceAssembler {

    private UpdatePlantCommandFromResourceAssembler() {
    }

    public static UpdatePlantCommand toCommandFromResource(
            Long plantId,
            UpdatePlantResource resource
    ) {

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