package com.plantsync.platform.profiles.application.acl;

import com.plantsync.platform.profiles.domain.model.aggregates.Profile;
import com.plantsync.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.plantsync.platform.profiles.domain.services.ProfileCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilesContextFacadeImplTest {

    @Mock
    private ProfileCommandService profileCommandService;

    @InjectMocks
    private ProfilesContextFacadeImpl profilesContextFacade;

    @Test
    void createProfileShouldReturnProfileIdWhenProfileIsCreated() {
        var mockProfile = mock(Profile.class);
        when(mockProfile.getId()).thenReturn(42L);
        when(profileCommandService.handle(any(CreateProfileCommand.class)))
            .thenReturn(Optional.of(mockProfile));

        Long result = profilesContextFacade.createProfile("Test Name", 1L, "basic", 30, "Male");

        assertEquals(42L, result);
        verify(profileCommandService).handle(any(CreateProfileCommand.class));
    }

    @Test
    void createProfileShouldHandleNullGender() {
        var mockProfile = mock(Profile.class);
        when(mockProfile.getId()).thenReturn(42L);
        when(profileCommandService.handle(any(CreateProfileCommand.class)))
            .thenReturn(Optional.of(mockProfile));

        Long result = profilesContextFacade.createProfile("Test Name", 1L, "basic", 30, null);

        assertEquals(42L, result);
        verify(profileCommandService).handle(any(CreateProfileCommand.class));
    }

    @Test
    void createProfileShouldReturnZeroWhenProfileCreationReturnsEmpty() {
        when(profileCommandService.handle(any(CreateProfileCommand.class)))
            .thenReturn(Optional.empty());

        Long result = profilesContextFacade.createProfile("Test Name", 1L, "basic", 30, "Male");

        assertEquals(0L, result);
        verify(profileCommandService).handle(any(CreateProfileCommand.class));
    }
}
