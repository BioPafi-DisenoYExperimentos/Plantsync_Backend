package com.plantsync.platform.plantguides.interfaces.rest.resources;

public record CreateGuideResource(

        String title,
        String name,
        String description,
        String topic,
        String type,
        String imageUrl

) {

    public CreateGuideResource {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}