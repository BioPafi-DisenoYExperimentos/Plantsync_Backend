package com.plantsync.platform.plantprofiles.interfaces.rest.resources;

public record CreatePlantHistoryResource(
        Long plantId,
        String type,
        String date,
        String time,
        Integer humidity
) {
}