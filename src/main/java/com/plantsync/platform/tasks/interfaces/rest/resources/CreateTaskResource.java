package com.plantsync.platform.tasks.interfaces.rest.resources;

/**
 * The type Create task resource.
 */
public record CreateTaskResource(

    String action,
    String date,
    Long plantId,
    Long profileId,
    Boolean completed

) {

}
