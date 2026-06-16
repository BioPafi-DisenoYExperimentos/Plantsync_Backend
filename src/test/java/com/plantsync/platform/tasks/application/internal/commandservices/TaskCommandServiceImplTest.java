package com.plantsync.platform.tasks.application.internal.commandservices;

import com.plantsync.platform.tasks.domain.exceptions.TaskCreationException;
import com.plantsync.platform.tasks.domain.exceptions.TaskDeletionException;
import com.plantsync.platform.tasks.domain.model.aggregates.Task;
import com.plantsync.platform.tasks.domain.model.commands.CreateTaskCommand;
import com.plantsync.platform.tasks.domain.model.commands.DeleteTaskCommand;
import com.plantsync.platform.tasks.domain.model.valueobjects.PlantId;
import com.plantsync.platform.tasks.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskCommandServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskCommandServiceImpl taskCommandService;

  @Test
  void handleCreateTaskCommandShouldSaveTaskAndReturnGeneratedId() {
    // Arrange
    var command = createTaskCommand();
    var taskCaptor = ArgumentCaptor.forClass(Task.class);

    // Act
    var result = taskCommandService.handle(command);

    // Assert
    verify(taskRepository).save(taskCaptor.capture());
    var savedTask = taskCaptor.getValue();
    assertEquals(command.date(), savedTask.getDate());
    assertEquals(command.action(), savedTask.getAction());
    assertEquals(command.completed(), savedTask.getCompleted());
    assertEquals(command.plantId(), savedTask.getPlantId());
    assertEquals(command.profileId(), savedTask.getProfileId());
    assertNull(result);
  }

  @Test
  void handleCreateTaskCommandShouldThrowTaskCreationExceptionWhenSaveFails() {
    // Arrange
    var command = createTaskCommand();
    when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("database unavailable"));

    // Act
    var exception = assertThrows(TaskCreationException.class, () -> taskCommandService.handle(command));

    // Assert
    assertEquals("Error saving task: database unavailable", exception.getMessage());
    verify(taskRepository).save(any(Task.class));
  }

  @Test
  void handleDeleteTaskCommandShouldDeleteTaskById() {
    // Arrange
    var command = new DeleteTaskCommand(1L);

    // Act
    taskCommandService.handle(command);

    // Assert
    verify(taskRepository).deleteById(command.taskId());
  }

  @Test
  void handleDeleteTaskCommandShouldThrowTaskDeletionExceptionWhenDeleteFails() {
    // Arrange
    var command = new DeleteTaskCommand(1L);
    doThrow(new RuntimeException("delete failed")).when(taskRepository).deleteById(command.taskId());

    // Act
    var exception = assertThrows(TaskDeletionException.class, () -> taskCommandService.handle(command));

    // Assert
    assertEquals("Error while deleting task: delete failed", exception.getMessage());
    verify(taskRepository).deleteById(command.taskId());
  }

  private CreateTaskCommand createTaskCommand() {
    return new CreateTaskCommand(
        LocalDate.of(2026, 1, 17),
        "Water plant",
        false,
        new PlantId(1L),
        new ProfileId(1L)
    );
  }
}
