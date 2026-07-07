package com.plantsync.platform.plantprofiles.application.internal.commandservices;

import com.plantsync.platform.plantprofiles.domain.exceptions.PlantCreationException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantDeletionException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantNotFoundException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantUpdateException;
import com.plantsync.platform.plantprofiles.domain.model.aggregates.Plant;
import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.commands.DeletePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.commands.UpdatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantRepository;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantHistoryRepository;
import com.plantsync.platform.tasks.infrastructure.persistence.jpa.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantCommandServiceImplTest {

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PlantHistoryRepository plantHistoryRepository;

    @InjectMocks
    private PlantCommandServiceImpl plantCommandService;

    @Test
    void handleCreatePlantCommandShouldSavePlantAndReturnGeneratedId() {
        // Arrange
        var command = createPlantCommand();

        // Act
        var result = plantCommandService.handle(command);

        // Assert
        assertNull(result);
        verify(plantRepository).save(any(Plant.class));
    }

    @Test
    void handleCreatePlantCommandShouldThrowPlantCreationExceptionWhenSaveFails() {
        // Arrange
        var command = createPlantCommand();
        when(plantRepository.save(any(Plant.class))).thenThrow(new RuntimeException("database unavailable"));

        // Act
        var exception = assertThrows(PlantCreationException.class, () -> plantCommandService.handle(command));

        // Assert
        assertEquals("Error saving plant: database unavailable", exception.getMessage());
        verify(plantRepository).save(any(Plant.class));
    }

    @Test
    void handleDeletePlantCommandShouldDeletePlantWhenItExists() {
        // Arrange
        var command = new DeletePlantCommand(1L);
        when(plantRepository.existsById(command.plantId())).thenReturn(true);

        // Act
        plantCommandService.handle(command);

        // Assert
        verify(plantRepository).existsById(command.plantId());
        verify(plantRepository).deleteById(command.plantId());
    }

    @Test
    void handleDeletePlantCommandShouldThrowWhenPlantDoesNotExist() {
        // Arrange
        var command = new DeletePlantCommand(99L);
        when(plantRepository.existsById(command.plantId())).thenReturn(false);

        // Act
        var exception = assertThrows(PlantNotFoundException.class, () -> plantCommandService.handle(command));

        // Assert
        assertEquals("Plant with ID 99 not found.", exception.getMessage());
        verify(plantRepository, never()).deleteById(command.plantId());
    }

    @Test
    void handleDeletePlantCommandShouldThrowPlantDeletionExceptionWhenDeleteFails() {
        // Arrange
        var command = new DeletePlantCommand(1L);
        when(plantRepository.existsById(command.plantId())).thenReturn(true);
        doThrow(new RuntimeException("delete failed")).when(plantRepository).deleteById(command.plantId());

        // Act
        var exception = assertThrows(PlantDeletionException.class, () -> plantCommandService.handle(command));

        // Assert
        assertEquals("Error while deleting plant: delete failed", exception.getMessage());
        verify(plantRepository).deleteById(command.plantId());
    }

    @Test
    void handleUpdatePlantCommandShouldUpdateAndReturnPlantWhenItExists() {
        // Arrange
        var command = createUpdatePlantCommand();
        var existingPlant = new Plant(createPlantCommand());
        when(plantRepository.findById(command.plantId())).thenReturn(Optional.of(existingPlant));
        when(plantRepository.save(existingPlant)).thenReturn(existingPlant);

        // Act
        var result = plantCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent());
        assertSame(existingPlant, result.get());
        assertEquals(command.name(), result.get().getName());
        assertEquals(command.species(), result.get().getSpecies());
        assertEquals(command.acquisitionDate(), result.get().getAcquisitionDate());
        assertEquals(command.humidity(), result.get().getHumidity());
        assertEquals(command.nextWateringDate(), result.get().getNextWateringDate());
        assertEquals(command.imageUrl(), result.get().getImageUrl());
        assertEquals(command.notificationsEnabled(), result.get().getNotificationsEnabled());
        assertEquals(command.profileId(), result.get().getProfileId());
        verify(plantRepository).save(existingPlant);
    }

    @Test
    void handleUpdatePlantCommandShouldReturnEmptyWhenPlantDoesNotExist() {
        // Arrange
        var command = createUpdatePlantCommand();
        when(plantRepository.findById(command.plantId())).thenReturn(Optional.empty());

        // Act
        var result = plantCommandService.handle(command);

        // Assert
        assertTrue(result.isEmpty());
        verify(plantRepository, never()).save(any(Plant.class));
    }

    @Test
    void handleUpdatePlantCommandShouldThrowPlantUpdateExceptionWhenSaveFails() {
        // Arrange
        var command = createUpdatePlantCommand();
        var existingPlant = new Plant(createPlantCommand());
        when(plantRepository.findById(command.plantId())).thenReturn(Optional.of(existingPlant));
        when(plantRepository.save(existingPlant)).thenThrow(new RuntimeException("update failed"));

        // Act
        var exception = assertThrows(PlantUpdateException.class, () -> plantCommandService.handle(command));

        // Assert
        assertEquals("Error while updating plant: update failed", exception.getMessage());
        verify(plantRepository).save(existingPlant);
    }

    private CreatePlantCommand createPlantCommand() {
        return new CreatePlantCommand(
                new PlantName("Monstera"),
                "Monstera deliciosa",
                LocalDate.of(2026, 1, 10),
                HumidityLevel.MEDIA,
                LocalDate.of(2026, 1, 17),
                "https://example.com/monstera.jpg",
                true,
                new ProfileId(1L)
        );
    }

    private UpdatePlantCommand createUpdatePlantCommand() {
        return new UpdatePlantCommand(
                1L,
                new PlantName("Updated Monstera"),
                "Monstera adansonii",
                LocalDate.of(2026, 2, 1),
                HumidityLevel.ALTA,
                LocalDate.of(2026, 2, 8),
                "https://example.com/updated-monstera.jpg",
                false,
                new ProfileId(2L)
        );
    }
}
