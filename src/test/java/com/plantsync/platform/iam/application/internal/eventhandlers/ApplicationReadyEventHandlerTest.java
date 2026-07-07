package com.plantsync.platform.iam.application.internal.eventhandlers;

import com.plantsync.platform.iam.domain.model.commands.SeedRolesCommands;
import com.plantsync.platform.iam.domain.services.RoleCommandService;
import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.infrastructure.persistence.jpa.repositories.GuideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationReadyEventHandlerTest {

  @Mock
  private RoleCommandService roleCommandService;

  @Mock
  private GuideRepository guideRepository;

  @Mock
  private ApplicationReadyEvent applicationReadyEvent;

  @Mock
  private ConfigurableApplicationContext applicationContext;

  @InjectMocks
  private ApplicationReadyEventHandler handler;

  @Test
  void onShouldSeedRolesWhenApplicationStarts() {
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("test-context");
    when(guideRepository.findAll()).thenReturn(List.of());

    handler.on(applicationReadyEvent);

    verify(roleCommandService).handle(SeedRolesCommands.INSTANCE);
  }

  @Test
  void onShouldUpdateExistingGuideWithOldImageUrl() {
    var existingGuide = mock(Guide.class);
    when(existingGuide.getImageUrl()).thenReturn("https://images.unsplash.com/photo-1614594975525-e45190c55d40?auto=format&fit=crop&w=400&q=80");
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("test-context");
    when(guideRepository.findAll()).thenReturn(List.of(existingGuide));
    when(guideRepository.count()).thenReturn(1L);

    handler.on(applicationReadyEvent);

    verify(guideRepository).save(existingGuide);
    verify(existingGuide).setImageUrl("https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=400&q=80");
  }

  @Test
  void onShouldNotUpdateExistingGuideWithDifferentUrl() {
    var existingGuide = mock(Guide.class);
    when(existingGuide.getImageUrl()).thenReturn("https://example.com/different-image.jpg");
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("test-context");
    when(guideRepository.findAll()).thenReturn(List.of(existingGuide));
    when(guideRepository.count()).thenReturn(1L);

    handler.on(applicationReadyEvent);

    verify(guideRepository, never()).save(any(Guide.class));
  }

  @Test
  void onShouldSeedInitialGuidesWhenDatabaseIsEmpty() {
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("test-context");
    when(guideRepository.findAll()).thenReturn(List.of());
    when(guideRepository.count()).thenReturn(0L);

    handler.on(applicationReadyEvent);

    verify(guideRepository, times(3)).save(any(Guide.class));
  }

  @Test
  void onShouldHandleExceptionGracefullyWhenGuideSeedingFails() {
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("test-context");
    when(guideRepository.findAll()).thenThrow(new RuntimeException("Database unavailable"));

    handler.on(applicationReadyEvent);

    verify(roleCommandService).handle(SeedRolesCommands.INSTANCE);
    verify(guideRepository).findAll();
  }
}
