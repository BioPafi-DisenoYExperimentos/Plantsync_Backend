package com.plantsync.platform.plantprofiles.domain.model.aggregates;

import com.plantsync.platform.plantprofiles.domain.model.commands.CreatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.commands.UpdatePlantCommand;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.HumidityLevel;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.PlantName;
import com.plantsync.platform.plantprofiles.domain.model.valueobjects.ProfileId;
import com.plantsync.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Aggregate root representing a Plant entity in the system.
 * Contains information about the plant's identity, care, and ownership profile.
 */
@Getter
@Setter
@Entity
public class Plant extends AuditableAbstractAggregateRoot<Plant> {

  /**
   * The name of the plant (value object).
   */
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "plant_name"))
  private PlantName name;

  /**
   * The species of the plant (e.g., Monstera, Cactus).
   */
  private String species;

  /**
   * The date the plant was acquired.
   */
  private LocalDate acquisitionDate;

  /**
   * The preferred humidity level for the plant.
   */
  @Enumerated(EnumType.STRING)
  private HumidityLevel humidity;

  /**
   * The next scheduled date for watering the plant.
   */
  private LocalDate nextWateringDate;

  /**
   * A Base64 or URL representing the image of the plant.
   */
  @Column(name = "image_url", columnDefinition = "LONGTEXT")
  private String imageUrl;

  /**
   * Indicates whether notifications are enabled for this plant.
   */
  private Boolean notificationsEnabled;

  /**
   * The ID of the profile that owns the plant.
   */
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "profile_id"))
  private ProfileId profileId;

  /**
   * Default constructor for JPA.
   */
  public Plant() {
  }

  /**
   * Constructs a new {@link Plant} from a {@link CreatePlantCommand}.
   *
   * @param command the command containing the data to create the plant
   */
  public Plant(CreatePlantCommand command) {
    this.name = command.name();
    this.species = command.species();
    this.acquisitionDate = command.acquisitionDate();
    this.humidity = command.humidity();
    this.nextWateringDate = command.nextWateringDate();
    this.imageUrl = command.imageUrl();
    this.notificationsEnabled = command.notificationsEnabled();
    this.profileId = command.profileId();
  }

  /**
   * Updates the plant's information.
   *
   * @param command the command containing the updated plant data
   * @return the updated plant instance
   */
  public Plant updateInformation(UpdatePlantCommand command) {
    this.name = command.name();
    this.species = command.species();
    this.acquisitionDate = command.acquisitionDate();
    this.humidity = command.humidity();
    this.nextWateringDate = command.nextWateringDate();
    this.imageUrl = command.imageUrl();
    this.notificationsEnabled = command.notificationsEnabled();
    this.profileId = command.profileId();

    return this;
  }
}
