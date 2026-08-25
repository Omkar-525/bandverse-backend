package com.bandverse.bandverse_backend.user;

import com.bandverse.bandverse_backend.business.artist.repository.ArtistRepository;
import com.bandverse.bandverse_backend.business.band.repository.BandRepository;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserRequest;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.business.user.entity.User;
import com.bandverse.bandverse_backend.business.user.repository.UserRepository;
import com.bandverse.bandverse_backend.business.user.service.UserServiceImpl;
import com.bandverse.bandverse_backend.infra.exception.UserAlreadyExistsException;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import com.bandverse.bandverse_backend.util.response_builders.success.SuccessResponseBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private BandRepository bandRepository;

    @Mock
    private SuccessResponseBuilder successResponseBuilder;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterNormalUser() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("user@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Test User");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.USER);

        String hashedPassword = "hashed-password";

        User savedUser = new User();

        UUID userId = UUID.randomUUID();

        savedUser.setId(userId);
        savedUser.setEmail(request.getEmail());
        savedUser.setPhone(request.getPhone());
        savedUser.setDisplayName(request.getDisplayName());
        savedUser.setPasswordHash(hashedPassword);
        savedUser.setRegistrationType(RegistrationType.USER);
        savedUser.setAccountStatus(AccountStatus.ACTIVE);

        RegisterUserResponse expectedResponse =
                RegisterUserResponse.builder()
                        .userId(userId)
                        .registrationType(RegistrationType.USER)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn(hashedPassword);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(
                successResponseBuilder.registerUser(
                        userId,
                        RegistrationType.USER,
                        AccountStatus.ACTIVE
                )
        ).thenReturn(expectedResponse);

        RegisterUserResponse actualResponse =
                userService.register(request);

        assertNotNull(actualResponse);

        assertEquals(
                userId,
                actualResponse.getUserId()
        );

        assertEquals(
                RegistrationType.USER,
                actualResponse.getRegistrationType()
        );

        assertEquals(
                AccountStatus.ACTIVE,
                actualResponse.getAccountStatus()
        );

        verify(passwordEncoder)
                .encode(request.getPassword());

        verify(userRepository)
                .save(any(User.class));

        verify(artistRepository, never())
                .save(any());

        verify(bandRepository, never())
                .save(any());
    }

    @Test
    void shouldCreateArtistProfileWhenRegistrationTypeIsArtist() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("artist@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Test Artist");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.ARTIST);

        String hashedPassword = "hashed-artist-password";

        User savedUser = new User();

        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail(request.getEmail());
        savedUser.setPhone(request.getPhone());
        savedUser.setDisplayName(request.getDisplayName());
        savedUser.setPasswordHash(hashedPassword);
        savedUser.setRegistrationType(RegistrationType.ARTIST);
        savedUser.setAccountStatus(AccountStatus.ACTIVE);

        RegisterUserResponse expectedResponse =
                RegisterUserResponse.builder()
                        .userId(savedUser.getId())
                        .registrationType(RegistrationType.ARTIST)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn(hashedPassword);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(
                successResponseBuilder.registerUser(
                        savedUser.getId(),
                        RegistrationType.ARTIST,
                        AccountStatus.ACTIVE
                )
        ).thenReturn(expectedResponse);

        RegisterUserResponse response =
                userService.register(request);

        assertNotNull(response);

        verify(passwordEncoder)
                .encode(request.getPassword());

        verify(artistRepository)
                .save(any());

        verify(bandRepository, never())
                .save(any());
    }

    @Test
    void shouldCreateBandProfileWhenRegistrationTypeIsBand() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("band@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Test Band");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.BAND);

        String hashedPassword = "hashed-band-password";

        User savedUser = new User();

        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail(request.getEmail());
        savedUser.setPhone(request.getPhone());
        savedUser.setDisplayName(request.getDisplayName());
        savedUser.setPasswordHash(hashedPassword);
        savedUser.setRegistrationType(RegistrationType.BAND);
        savedUser.setAccountStatus(AccountStatus.ACTIVE);

        RegisterUserResponse expectedResponse =
                RegisterUserResponse.builder()
                        .userId(savedUser.getId())
                        .registrationType(RegistrationType.BAND)
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn(hashedPassword);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        when(
                successResponseBuilder.registerUser(
                        savedUser.getId(),
                        RegistrationType.BAND,
                        AccountStatus.ACTIVE
                )
        ).thenReturn(expectedResponse);

        RegisterUserResponse response =
                userService.register(request);

        assertNotNull(response);

        verify(passwordEncoder)
                .encode(request.getPassword());

        verify(bandRepository)
                .save(any());

        verify(artistRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectDuplicateEmail() {

        RegisterUserRequest request = new RegisterUserRequest();

        request.setEmail("duplicate@bandverse.test");
        request.setPhone("9999999999");
        request.setDisplayName("Duplicate User");
        request.setPassword("TestPassword123!");
        request.setRegistrationType(RegistrationType.USER);

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(any());

        verify(artistRepository, never())
                .save(any());

        verify(bandRepository, never())
                .save(any());
    }
}