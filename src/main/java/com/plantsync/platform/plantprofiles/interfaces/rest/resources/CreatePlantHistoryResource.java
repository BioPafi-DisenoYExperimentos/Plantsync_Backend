package com.plantsync.platform.plantprofiles.interfaces.rest.resources;

/**
 * The type Create plant history resource.
 */
public record CreatePlantHistoryResource(


    Long plantId,
    String type,
    String date,
    String time,
    Integer humidity

) {

}
