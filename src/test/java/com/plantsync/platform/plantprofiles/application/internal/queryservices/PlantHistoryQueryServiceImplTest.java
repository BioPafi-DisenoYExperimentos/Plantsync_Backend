package com.plantsync.platform.plantprofiles.application.internal.queryservices;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.PlantHistory;
import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantHistoryCommand;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetAllPlantHistoriesByPlantIdQuery;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetPlantHistoryByIdQuery;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetPlantHistoryByPlantIdQuery;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantId;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantHistoryQueryServiceImplTest {

  @Mock
  private PlantHistoryRepository plantHistoryRepository;

  @InjectMocks
  private PlantHistoryQueryServiceImpl plantHistoryQueryService;

  @Test
  void handleGetPlantHistoryByPlantIdQueryShouldReturnHistoryWhenItExists() {
    // Arrange
    var query = new GetPlantHistoryByPlantIdQuery(1L);
    var plantHistory = createPlantHistory();
    var plantId = new PlantId(query.plantId());
    when(plantHistoryRepository.findFirstByPlantId(plantId)).thenReturn(Optional.of(plantHistory));

    // Act
    var result = plantHistoryQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertSame(plantHistory, result.get());
    verify(plantHistoryRepository).findFirstByPlantId(plantId);
  }

  @Test
  void handleGetPlantHistoryByPlantIdQueryShouldReturnEmptyWhenHistoryDoesNotExist() {
    // Arrange
    var query = new GetPlantHistoryByPlantIdQuery(99L);
    var plantId = new PlantId(query.plantId());
    when(plantHistoryRepository.findFirstByPlantId(plantId)).thenReturn(Optional.empty());

    // Act
    var result = plantHistoryQueryService.handle(query);

    // Assert
    assertTrue(result.isEmpty());
    verify(plantHistoryRepository).findFirstByPlantId(plantId);
  }

  @Test
  void handleGetAllPlantHistoriesByPlantIdQueryShouldReturnHistoriesForPlant() {
    // Arrange
    var query = new GetAllPlantHistoriesByPlantIdQuery(new PlantId(1L));
    var histories = List.of(createPlantHistory());
    when(plantHistoryRepository.findByPlantId(query.plantId())).thenReturn(histories);

    // Act
    var result = plantHistoryQueryService.handle(query);

    // Assert
    assertEquals(histories, result);
    verify(plantHistoryRepository).findByPlantId(query.plantId());
  }

  @Test
  void handleGetPlantHistoryByIdQueryShouldReturnHistoryWhenItExists() {
    var query = new GetPlantHistoryByIdQuery(1L);
    var plantHistory = createPlantHistory();
    when(plantHistoryRepository.findById(query.id())).thenReturn(Optional.of(plantHistory));

    var result = plantHistoryQueryService.handle(query);

    assertTrue(result.isPresent());
    assertSame(plantHistory, result.get());
    verify(plantHistoryRepository).findById(query.id());
  }

  @Test
  void handleGetPlantHistoryByIdQueryShouldReturnEmptyWhenHistoryDoesNotExist() {
    var query = new GetPlantHistoryByIdQuery(99L);
    when(plantHistoryRepository.findById(query.id())).thenReturn(Optional.empty());

    var result = plantHistoryQueryService.handle(query);

    assertTrue(result.isEmpty());
    verify(plantHistoryRepository).findById(query.id());
  }

  private PlantHistory createPlantHistory() {
    return new PlantHistory(new CreatePlantHistoryCommand(
        new PlantId(1L),
        "WATERED",
        LocalDate.of(2026, 1, 17),
        LocalTime.of(8, 30),
        65
    ));
  }
}
