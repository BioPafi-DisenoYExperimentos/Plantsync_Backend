package com.plantsync.platform.plantguides.application.internal.commandservices;

import com.plantsync.platform.plantguides.domain.exceptions.GuideCreationException;
import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.domain.model.commands.CreateGuideCommand;
import com.plantsync.platform.plantguides.infrastructure.persistence.jpa.repositories.GuideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuideCommandServiceImplTest {

  @Mock
  private GuideRepository guideRepository;

  @InjectMocks
  private GuideCommandServiceImpl guideCommandService;

  @Test
  void handleCreateGuideCommandShouldSaveGuideAndReturnGeneratedId() {
    // Arrange
    var command = createGuideCommand();
    var guideCaptor = ArgumentCaptor.forClass(Guide.class);

    // Act
    var result = guideCommandService.handle(command);

    // Assert
    verify(guideRepository).save(guideCaptor.capture());
    var savedGuide = guideCaptor.getValue();
    assertEquals(command.title(), savedGuide.getTitle());
    assertEquals(command.name(), savedGuide.getName());
    assertEquals(command.description(), savedGuide.getDescription());
    assertEquals(command.topic(), savedGuide.getTopic());
    assertEquals(command.type(), savedGuide.getType());
    assertEquals(command.imageUrl(), savedGuide.getImageUrl());
    assertNull(result);
  }

  @Test
  void handleCreateGuideCommandShouldThrowGuideCreationExceptionWhenSaveFails() {
    // Arrange
    var command = createGuideCommand();
    when(guideRepository.save(any(Guide.class))).thenThrow(new RuntimeException("database unavailable"));

    // Act
    var exception = assertThrows(GuideCreationException.class, () -> guideCommandService.handle(command));

    // Assert
    assertEquals("Error saving guide: database unavailable", exception.getMessage());
    verify(guideRepository).save(any(Guide.class));
  }

  private CreateGuideCommand createGuideCommand() {
    return new CreateGuideCommand(
        "Watering Basics",
        "PlantSync",
        "A practical guide for watering indoor plants.",
        "Care",
        "Article",
        "https://example.com/watering-basics.jpg"
    );
  }
}
