package com.plantsync.platform.plantprofiles.interfaces.rest.assemblers;

import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantHistoryCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantId;
import com.plantsync.platform.plantprofiles.interfaces.rest.resources.CreatePlantHistoryResource;

import java.time.LocalDate;
import java.time.LocalTime;

public final class CreatePlantHistoryCommandFromResourceAssembler {

    private CreatePlantHistoryCommandFromResourceAssembler() {
    }

    public static CreatePlantHistoryCommand toCommandFromResource(
            CreatePlantHistoryResource resource
    ) {

        return new CreatePlantHistoryCommand(
                new PlantId(resource.plantId()),
                resource.type(),
                LocalDate.parse(resource.date()),
                LocalTime.parse(resource.time()),
                resource.humidity()
        );
    }
}