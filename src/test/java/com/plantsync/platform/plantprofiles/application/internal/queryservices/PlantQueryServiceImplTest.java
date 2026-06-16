package com.plantsync.platform.plantprofiles.application.internal.queryservices;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.Plant;
import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetAllPlantsByProfileIdQuery;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetAllPlantsQueries;
import com.plantsync.platform.plantprofiles.domain.model.queries.GetPlantByIdQuery;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantHistoryRepository;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantQueryServiceImplTest {

  @Mock
  private PlantRepository plantRepository;

  @Mock
  private PlantHistoryRepository plantHistoryRepository;

  @InjectMocks
  private PlantQueryServiceImpl plantQueryService;

  @Test
  void handleGetAllPlantsQueryShouldReturnAllPlants() {
    // Arrange
    var query = GetAllPlantsQueries.INSTANCE;
    var plants = List.of(createPlant());
    when(plantRepository.findAll()).thenReturn(plants);

    // Act
    var result = plantQueryService.handle(query);

    // Assert
    assertEquals(plants, result);
    verify(plantRepository).findAll();
  }

  @Test
  void handleGetAllPlantsByProfileIdQueryShouldReturnPlantsForProfile() {
    // Arrange
    var query = new GetAllPlantsByProfileIdQuery(new ProfileId(1L));
    var plants = List.of(createPlant());
    when(plantRepository.findByProfileId(query.profileId())).thenReturn(plants);

    // Act
    var result = plantQueryService.handle(query);

    // Assert
    assertEquals(plants, result);
    verify(plantRepository).findByProfileId(query.profileId());
  }

  @Test
  void handleGetPlantByIdQueryShouldReturnPlantWhenItExists() {
    // Arrange
    var query = new GetPlantByIdQuery(1L);
    var plant = createPlant();
    when(plantRepository.existsById(query.plantId())).thenReturn(true);
    when(plantRepository.findById(query.plantId())).thenReturn(Optional.of(plant));

    // Act
    var result = plantQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertSame(plant, result.get());
    verify(plantRepository).existsById(query.plantId());
    verify(plantRepository).findById(query.plantId());
  }

  @Test
  void handleGetPlantByIdQueryShouldReturnEmptyWhenPlantDoesNotExist() {
    // Arrange
    var query = new GetPlantByIdQuery(99L);
    when(plantRepository.existsById(query.plantId())).thenReturn(false);

    // Act
    var result = plantQueryService.handle(query);

    // Assert
    assertTrue(result.isEmpty());
    verify(plantRepository, never()).findById(query.plantId());
  }

  private Plant createPlant() {
    return new Plant(new CreatePlantCommand(
        new PlantName("Monstera"),
        "Monstera deliciosa",
        LocalDate.of(2026, 1, 10),
        HumidityLevel.MEDIA,
        LocalDate.of(2026, 1, 17),
        "https://example.com/monstera.jpg",
        true,
        new ProfileId(1L)
    ));
  }
}
