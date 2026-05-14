package com.plantsync.platform.tasks.interfaces.rest.resources;

public record CreateTaskResource(

        String action,
        String date,
        Long plantId,
        Long profileId,
        Boolean completed

) {
}