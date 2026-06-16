package com.plantsync.platform.tasks.application.internal.queryservices;

import com.plantsync.platform.tasks.domain.model.aggregates.Task;
import com.plantsync.platform.tasks.domain.model.commands.CreateTaskCommand;
import com.plantsync.platform.tasks.domain.model.queries.GetAllTasksQueries;
import com.plantsync.platform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.plantsync.platform.tasks.domain.model.valueobjects.PlantId;
import com.plantsync.platform.tasks.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskQueryServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskQueryServiceImpl taskQueryService;

  @Test
  void handleGetAllTasksQueryShouldReturnAllTasks() {
    // Arrange
    var query = GetAllTasksQueries.INSTANCE;
    var tasks = List.of(createTask());
    when(taskRepository.findAll()).thenReturn(tasks);

    // Act
    var result = taskQueryService.handle(query);

    // Assert
    assertEquals(tasks, result);
    verify(taskRepository).findAll();
  }

  @Test
  void handleGetTaskByIdQueryShouldReturnTaskWhenItExists() {
    // Arrange
    var query = new GetTaskByIdQuery(1L);
    var task = createTask();
    when(taskRepository.findById(query.taskId())).thenReturn(Optional.of(task));

    // Act
    var result = taskQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertSame(task, result.get());
    verify(taskRepository).findById(query.taskId());
  }

  private Task createTask() {
    return new Task(new CreateTaskCommand(
        LocalDate.of(2026, 1, 17),
        "Water plant",
        false,
        new PlantId(1L),
        new ProfileId(1L)
    ));
  }
}
