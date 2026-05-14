package com.plantsync.platform.tasks.domain.model.commands;

import com.plantsync.platform.tasks.domain.model.valueobjects.PlantId;
import com.plantsync.platform.tasks.domain.model.valueobjects.ProfileId;

import java.time.LocalDate;

public record CreateTaskCommand(

        LocalDate date,
        String action,
        Boolean completed,
        PlantId plantId,
        ProfileId profileId

) {
}