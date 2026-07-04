package com.plantsync.platform.iam.application.internal.eventhandlers;

import com.plantsync.platform.iam.domain.model.commands.SeedRolesCommands;
import com.plantsync.platform.iam.domain.services.RoleCommandService;
import com.plantsync.platform.plantguides.infrastructure.persistence.jpa.repositories.GuideRepository;
import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * ApplicationReadyEventHandler class.
 * This class is used to handle the ApplicationReadyEvent.
 */
@Service
public class ApplicationReadyEventHandler {
  private final RoleCommandService roleCommandService;
  private final GuideRepository guideRepository;
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

  /**
   * ApplicationReadyEventHandler constructor.
   * Injecting RoleCommandService and GuideRepository.
   */
  public ApplicationReadyEventHandler(RoleCommandService roleCommandService, GuideRepository guideRepository) {
    this.roleCommandService = roleCommandService;
    this.guideRepository = guideRepository;
  }

  /**
   * Handle the ApplicationReadyEvent.
   * This method is used to seed the roles and guides.
   *
   * @param event the ApplicationReadyEvent the event to handle
   */
  @EventListener
  public void on(ApplicationReadyEvent event) {
    var applicationName = event.getApplicationContext().getId();
    LOGGER.info("Starting to verify if roles seeding is needed for {} at {}",
        applicationName, currentTimestamp());
    var seedRolesCommand = SeedRolesCommands.INSTANCE;
    roleCommandService.handle(seedRolesCommand);
    LOGGER.info("Roles seeding verification finished for {} at {}",
        applicationName, currentTimestamp());

    seedGuides();
  }

  private void seedGuides() {
    try {
      var oldImageUrl = "https://images.unsplash.com/photo-1614594975525-e45190c55d40?auto=format&fit=crop&w=400&q=80";
      var newImageUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=400&q=80";

      // Update existing guides that use the broken URL
      var guides = guideRepository.findAll();
      for (var guide : guides) {
        if (oldImageUrl.equals(guide.getImageUrl())) {
          guide.setImageUrl(newImageUrl);
          guideRepository.save(guide);
          LOGGER.info("Updated existing Monstera guide image URL from old to working one.");
        }
      }

      if (guideRepository.count() == 0) {
        LOGGER.info("Seeding initial guides in the database...");
        guideRepository.save(new Guide(
            "Cuidado de Monstera Deliciosa",
            "Experto BioDemeter",
            "Aprende los cuidados esenciales para mantener tu Monstera verde y saludable. Consejos sobre luz indirecta y frecuencia de riego.",
            "Riego y Luz",
            "Artículo",
            newImageUrl
        ));
        guideRepository.save(new Guide(
            "Guía del Cactus de Desierto",
            "Especialista en Suculentas",
            "El secreto para no ahogar a tu cactus. Aprende sobre el sustrato drenante y los ciclos de riego ideales.",
            "Sustratos",
            "Manual",
            "https://images.unsplash.com/photo-1459411552884-841db9b3cc2a?auto=format&fit=crop&w=400&q=80"
        ));
        guideRepository.save(new Guide(
            "Propagación de Plantas en Agua",
            "Botánico Urbano",
            "Paso a paso para multiplicar tus plantas favoritas usando solo agua. Ideal para potus y filodendros.",
            "Propagación",
            "Tutorial",
            "https://images.unsplash.com/photo-1545241047-6083a3684587?auto=format&fit=crop&w=400&q=80"
        ));
        LOGGER.info("Initial guides seeded successfully.");
      }
    } catch (Exception e) {
      LOGGER.error("Failed to seed or update initial guides: {}", e.getMessage());
    }
  }

  private Timestamp currentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
}