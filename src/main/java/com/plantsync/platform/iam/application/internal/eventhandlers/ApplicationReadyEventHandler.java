package com.plantsync.platform.iam.application.internal.eventhandlers;

import com.plantsync.platform.iam.domain.model.commands.SeedRolesCommands;
import com.plantsync.platform.iam.domain.services.RoleCommandService;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

  /**
   * ApplicationReadyEventHnadler class.
   * This class is used to call command role service.
   * */

  public ApplicationReadyEventHandler(RoleCommandService roleCommandService) {
    this.roleCommandService = roleCommandService;
  }

  /**
   * Handle the ApplicationReadyEvent.
   * This method is used to seed the roles.
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
  }

  private Timestamp currentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
}