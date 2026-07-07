package com.plantsync.platform.tasks.interfaces.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.plantsync.platform.tasks.domain.model.queries.GetAllTasksQueries;
import com.plantsync.platform.tasks.domain.services.TaskQueryService;
import com.plantsync.platform.tasks.interfaces.rest.assemblers.TaskResourceFromEntityAssembler;
import com.plantsync.platform.tasks.interfaces.rest.resources.TaskResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for querying tasks.
 * Provides endpoints to retrieve tasks.
 */
@RestController
@RequestMapping(value = "/api/v1/tasks", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Tasks", description = "Task Management Endpoints")
public class TaskQueryController {

  private final TaskQueryService taskQueryService;

  /**
   * Constructor for TaskQueryController.
   *
   * @param taskQueryService The task query service.
   */
  public TaskQueryController(TaskQueryService taskQueryService) {
    this.taskQueryService = taskQueryService;
  }

  /**
   * Retrieves all tasks.
   *
   * @return A list of {@link TaskResource} if found.
   */
  @GetMapping
  @Operation(summary = "Get all tasks")
  @ApiResponse(responseCode = "200", description = "Tasks found")
  @ApiResponse(responseCode = "404", description = "No tasks found")
  public ResponseEntity<List<TaskResource>> getAllTasks() {
    var tasks = taskQueryService.handle(GetAllTasksQueries.INSTANCE);

    if (tasks.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var resources = tasks.stream()
        .map(TaskResourceFromEntityAssembler::toResourceFromEntity)
        .toList();

    return ResponseEntity.ok(resources);
  }
}
