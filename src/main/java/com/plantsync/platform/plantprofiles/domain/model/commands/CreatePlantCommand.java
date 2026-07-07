package com.plantsync.platform.plantprofiles.domain.model.commands;

import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import java.time.LocalDate;

/**
 * Create Plant command.
 * */
public record CreatePlantCommand(

    PlantName name,

    String species,

    LocalDate acquisitionDate,

    HumidityLevel humidity,

    LocalDate nextWateringDate,

    String imageUrl,

    Boolean notificationsEnabled,

    ProfileId profileId


) {


}