package com.plantsync.platform.plantguides.application.internal.queryservices;

import com.plantsync.platform.plantguides.domain.model.aggregates.Guide;
import com.plantsync.platform.plantguides.domain.model.queries.GetAllGuidesQueries;
import com.plantsync.platform.plantguides.domain.model.queries.GetGuideByIdQuery;
import com.plantsync.platform.plantguides.infrastructure.persistence.jpa.repositories.GuideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuideQueryServiceImplTest {

  @Mock
  private GuideRepository guideRepository;

  @InjectMocks
  private GuideQueryServiceImpl guideQueryService;

  @Test
  void handleGetAllGuidesQueryShouldReturnAllGuides() {
    // Arrange
    var query = GetAllGuidesQueries.INSTANCE;
    var guides = List.of(createGuide());
    when(guideRepository.findAll()).thenReturn(guides);

    // Act
    var result = guideQueryService.handle(query);

    // Assert
    assertEquals(guides, result);
    verify(guideRepository).findAll();
  }

  @Test
  void handleGetGuideByIdQueryShouldReturnGuideWhenItExists() {
    // Arrange
    var query = new GetGuideByIdQuery(1L);
    var guide = createGuide();
    when(guideRepository.findById(query.guideId())).thenReturn(Optional.of(guide));

    // Act
    var result = guideQueryService.handle(query);

    // Assert
    assertTrue(result.isPresent());
    assertSame(guide, result.get());
    verify(guideRepository).findById(query.guideId());
  }

  private Guide createGuide() {
    return new Guide(
        "Watering Basics",
        "PlantSync",
        "A practical guide for watering indoor plants.",
        "Care",
        "Article",
        "https://example.com/watering-basics.jpg"
    );
  }
}
