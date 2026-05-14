package com.plantsync.platform.plantprofiles.application.internal.commandservices;

import com.plantsync.platform.plantprofiles.domain.exceptions.PlantCreationException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantDeletionException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantNotFoundException;
import com.plantsync.platform.plantprofiles.domain.exceptions.PlantUpdateException;
import com.plantsync.platform.plantprofiles.domain.model.aggregates.Plant;
import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.commands.DeletePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.commands.UpdatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.services.PlantCommandService;
import com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlantCommandServiceImpl implements PlantCommandService {

    private final PlantRepository plantRepository;

    public PlantCommandServiceImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public Long handle(CreatePlantCommand command) {
        var plant = new Plant(command);

        try {
            plantRepository.save(plant);
        } catch (Exception e) {
            throw new PlantCreationException(e.getMessage());
        }

        return plant.getId();
    }

    @Override
    public void handle(DeletePlantCommand command) {
        if (!plantRepository.existsById(command.plantId())) {
            throw new PlantNotFoundException(command.plantId());
        }

        try {
            plantRepository.deleteById(command.plantId());
        } catch (Exception e) {
            throw new PlantDeletionException(e.getMessage());
        }
    }

    @Override
    public Optional<Plant> handle(UpdatePlantCommand command) {
        var result = plantRepository.findById(command.plantId());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        var plantToUpdate = result.get();

        try {
            var updatedPlant = plantRepository.save(
                    plantToUpdate.updateInformation(command)
            );

            return Optional.of(updatedPlant);
        } catch (Exception e) {
            throw new PlantUpdateException(e.getMessage());
        }
    }
}