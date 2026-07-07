package com.plantsync.platform.plantprofiles.infrastructure.persistence.jpa.repositories;

import com.plantsync.platform.plantprofiles.domain.model.aggregates.PlantHistory;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link PlantHistory} entities.
 */
public interface PlantHistoryRepository extends JpaRepository<PlantHistory, Long> {
  /**
   * Finds all plant history records associated with a specific plant ID.
   *
   * @param plantId The ID of the plant.
   * @return A list of {@link PlantHistory} records.
   */
  List<PlantHistory> findByPlantId(PlantId plantId);

  /**
   * Finds the most recent plant history record for a specific plant ID.
   *
   * @param plantId The ID of the plant.
   * @return An {@link Optional} containing the {@link PlantHistory} if found.
   */
  Optional<PlantHistory> findFirstByPlantId(PlantId plantId);

  @org.springframework.transaction.annotation.Transactional
  void deleteByPlantId(PlantId plantId);
}