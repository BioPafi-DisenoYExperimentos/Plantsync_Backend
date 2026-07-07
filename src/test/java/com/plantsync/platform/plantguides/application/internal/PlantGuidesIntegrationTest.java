package com.plantsync.platform.plantguides.application.internal;

import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.domain.model.commands.CreateGuideCommand;
import com.plantsync.platform.plantguides.domain.model.queries.GetAllGuidesQueries;
import com.plantsync.platform.plantguides.domain.model.queries.GetGuideByIdQuery;
import com.plantsync.platform.plantguides.domain.services.GuideCommandService;
import com.plantsync.platform.plantguides.domain.services.GuideQueryService;
import com.plantsync.platform.plantguides.infrastructure.persistence.jpa.repositories.GuideRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.profiles.active=test",
    "spring.datasource.url=jdbc:h2:mem:plantsync-guide-test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.jpa.show-sql=false",
    "spring.flyway.enabled=false"
})
class PlantGuidesIntegrationTest {

  @Autowired
  private GuideCommandService guideCommandService;

  @Autowired
  private GuideQueryService guideQueryService;

  @Autowired
  private GuideRepository guideRepository;

  @BeforeEach
  void setUp() {
    guideRepository.deleteAll();
  }

  @Test
  @Transactional
  void createGuideShouldPersistGuideAndAllowQueryById() {
    // Arrange
    CreateGuideCommand command = new CreateGuideCommand(
        "How to care for a Monstera",
        "Jane Doe",
        "A comprehensive guide for Monstera plants.",
        "Indoor Plants",
        "Tutorial",
        "http://example.com/monstera.jpg"
    );

    // Act
    Long createdGuideId = guideCommandService.handle(command);
    Optional<Guide> queriedGuide = guideQueryService.handle(new GetGuideByIdQuery(createdGuideId));

    // Assert
    assertNotNull(createdGuideId);
    assertTrue(queriedGuide.isPresent());
    assertEquals(command.title(), queriedGuide.get().getTitle());
    assertEquals(command.name(), queriedGuide.get().getName());
    assertEquals(command.topic(), queriedGuide.get().getTopic());
  }

  @Test
  @Transactional
  void getAllGuidesShouldReturnAllCreatedGuides() {
    // Arrange
    CreateGuideCommand command1 = new CreateGuideCommand("Guide 1", "Author A", "Desc 1", "Topic 1", "Type 1", "url1");
    CreateGuideCommand command2 = new CreateGuideCommand("Guide 2", "Author B", "Desc 2", "Topic 2", "Type 2", "url2");

    Long id1 = guideCommandService.handle(command1);
    Long id2 = guideCommandService.handle(command2);

    // Act
    List<Guide> guides = guideQueryService.handle(GetAllGuidesQueries.INSTANCE);

    // Assert
    assertNotNull(guides);
    assertEquals(2, guides.size());
    assertTrue(guides.stream().anyMatch(guide -> guide.getId().equals(id1)));
    assertTrue(guides.stream().anyMatch(guide -> guide.getId().equals(id2)));
  }
}