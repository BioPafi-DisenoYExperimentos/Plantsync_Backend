package com.plantsync.platform.tasks.interfaces.rest.assemblers;

import com.plantsync.platform.tasks.domain.model.aggregates.Task;
import com.plantsync.platform.tasks.interfaces.rest.resources.TaskResource;

/**
 * The type Task resource from entity assembler.
 */
public class TaskResourceFromEntityAssembler {

  private TaskResourceFromEntityAssembler() {
    // Utility class
  }
  /**
   * To resource from entity task resource.
   *
   * @param entity the entity
   * @return the task resource
   */
  public static TaskResource toResourceFromEntity(Task entity) {

    return new TaskResource(
        entity.getId(),
        entity.getAction(),
        entity.getDate().toString(),
        entity.getPlantId().value(),
        entity.getProfileId().value(),
        entity.getCompleted()

    );
  }

}
