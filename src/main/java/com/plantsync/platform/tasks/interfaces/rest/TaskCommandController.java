package com.plantsync.platform.tasks.interfaces.rest;


import com.plantsync.platform.tasks.domain.model.commands.DeleteTaskCommand;
import com.plantsync.platform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.plantsync.platform.tasks.domain.services.TaskCommandService;
import com.plantsync.platform.tasks.domain.services.TaskQueryService;
import com.plantsync.platform.tasks.interfaces.rest.assemblers.CreateTaskCommandFromResourceAssembler;
import com.plantsync.platform.tasks.interfaces.rest.assemblers.TaskResourceFromEntityAssembler;
import com.plantsync.platform.tasks.interfaces.rest.resources.CreateTaskResource;
import com.plantsync.platform.tasks.interfaces.rest.resources.TaskResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/tasks", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Tasks", description = "Available Task Endpoints")
public class TaskCommandController {


    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;


    public TaskCommandController(TaskCommandService taskCommandService, TaskQueryService taskQueryService) {
        this.taskCommandService = taskCommandService;
        this.taskQueryService = taskQueryService;

    }


    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<TaskResource> createTask(@Valid @RequestBody CreateTaskResource resource) {
        var createTaskCommand = CreateTaskCommandFromResourceAssembler.toCommandFromResource(resource);
        var taskId = taskCommandService.handle(createTaskCommand);
        if (taskId == null || taskId == 0L) return ResponseEntity.badRequest().build();
        var getTaskByIdQuery = new GetTaskByIdQuery(taskId);
        var task = taskQueryService.handle(getTaskByIdQuery);
        if (task.isEmpty()) return ResponseEntity.notFound().build();
        var taskEntity = task.get();
        var taskResource = TaskResourceFromEntityAssembler.toResourceFromEntity(taskEntity);
        return new ResponseEntity<>(taskResource, HttpStatus.CREATED);
    }




    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Delete task with a given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        var deleteTaskCommand = new DeleteTaskCommand(taskId);
        taskCommandService.handle(deleteTaskCommand);
        return ResponseEntity.noContent().build();
    }



}