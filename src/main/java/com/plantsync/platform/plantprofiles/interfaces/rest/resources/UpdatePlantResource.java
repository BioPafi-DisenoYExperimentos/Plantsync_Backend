package com.plantsync.platform.plantprofiles.interfaces.rest.resources;

/**
 * The type Update plant resource.
 */
public record UpdatePlantResource(

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