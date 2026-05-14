package com.plantsync.platform.plantprofiles.interfaces.rest.resources;

public record CreatePlantResource(

        String name,
        String species,
        String acquisitionDate,
        String humidity,
        String nextWateringDate,
        String imageUrl,
        Boolean notificationsEnabled,
        Long profileId

) {
}