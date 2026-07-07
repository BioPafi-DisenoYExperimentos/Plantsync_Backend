package com.plantsync.platform.plantprofiles.application.internal.commandservices;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.PlantHistory;
import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantHistoryCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantId;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantHistoryCommandServiceImplTest {

  @Mock
  private PlantHistoryRepository plantHistoryRepository;

  @InjectMocks
  private PlantHistoryCommandServiceImpl plantHistoryCommandService;

  @Test
  void handleCreatePlantHistoryCommandShouldSaveHistoryAndReturnGeneratedId() {
    // Arrange
    var command = createPlantHistoryCommand();
    var plantHistoryCaptor = ArgumentCaptor.forClass(PlantHistory.class);

    // Act
    var result = plantHistoryCommandService.handle(command);

    // Assert
    verify(plantHistoryRepository).save(plantHistoryCaptor.capture());
    var savedHistory = plantHistoryCaptor.getValue();
    assertEquals(command.plantId(), savedHistory.getPlantId());
    assertEquals(command.type(), savedHistory.getType());
    assertEquals(command.date(), savedHistory.getDate());
    assertEquals(command.time(), savedHistory.getTime());
    assertEquals(command.humidity(), savedHistory.getHumidity());
    assertNull(result);
  }

  @Test
  void handleCreatePlantHistoryCommandShouldThrowWhenSaveFails() {
    // Arrange
    var command = createPlantHistoryCommand();
    when(plantHistoryRepository.save(any(PlantHistory.class))).thenThrow(new RuntimeException("database unavailable"));

    // Act
    var exception = assertThrows(IllegalArgumentException.class, () -> plantHistoryCommandService.handle(command));

    // Assert
    assertEquals("Error saving plant history: database unavailable", exception.getMessage());
    verify(plantHistoryRepository).save(any(PlantHistory.class));
  }

  private CreatePlantHistoryCommand createPlantHistoryCommand() {
    return new CreatePlantHistoryCommand(
        new PlantId(1L),
        "WATERED",
        LocalDate.of(2026, 1, 17),
        LocalTime.of(8, 30),
        65
    );
  }
}
