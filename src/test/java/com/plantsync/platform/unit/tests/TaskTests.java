package com.plantsync.platform.unit.tests;


import com.plantsync.platform.tasks.domain.model.aggregates.Task;
import com.plantsync.platform.tasks.domain.model.commands.CreateTaskCommand;
import com.plantsync.platform.tasks.domain.model.valueobjects.PlantId;
import com.plantsync.platform.tasks.domain.model.valueobjects.ProfileId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTests {

  @Test
  void constructor_WithValidData_ShouldCreateTaskCorrectly() {
    // Arrange
    LocalDate date = LocalDate.of(2025, 7, 3);
    String action = "Watering";
    Boolean completed = false;
    PlantId plantId = new PlantId(1L);
    ProfileId profileId = new ProfileId(1L);

    CreateTaskCommand command = new CreateTaskCommand(date, action, completed, plantId, profileId);

    // Act
    Task task = new Task(command);

    // Assert
    assertEquals(date, task.getDate());
    assertEquals(action, task.getAction());
    assertEquals(completed, task.getCompleted());
    assertEquals(plantId, task.getPlantId());
    assertEquals(profileId, task.getProfileId());
  }

  @Test
  void constructor_WithNullAction_ShouldAcceptNullIfNoValidation() {

    CreateTaskCommand command = new CreateTaskCommand(
        LocalDate.now(), null, false, new PlantId(1L), new ProfileId(1L)
    );

    Task task = new Task(command);

    assertNull(task.getAction());
  }
}
