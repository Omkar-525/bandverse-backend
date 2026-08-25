package com.bandverse.bandverse_backend.business.user.service;

import com.bandverse.bandverse_backend.business.artist.entity.Artist;
import com.bandverse.bandverse_backend.business.artist.repository.ArtistRepository;
import com.bandverse.bandverse_backend.business.band.entity.Band;
import com.bandverse.bandverse_backend.business.band.repository.BandRepository;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserRequest;
import com.bandverse.bandverse_backend.business.user.dto.RegisterUserResponse;
import com.bandverse.bandverse_backend.infra.exception.UserAlreadyExistsException;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import com.bandverse.bandverse_backend.business.user.entity.User;
import com.bandverse.bandverse_backend.business.user.repository.UserRepository;
import com.bandverse.bandverse_backend.util.response_builders.success.SuccessResponseBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final SuccessResponseBuilder successResponseBuilder;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final BandRepository bandRepository;

    @Override
    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {

        log.info(
                "User registration started. registrationType={}",
                request.getRegistrationType()
        );

        if (userRepository.existsByEmail(request.getEmail())) {

            log.warn(
                    "User registration rejected. Reason=duplicate_email"
            );

            throw new UserAlreadyExistsException(
                    "A user with this email already exists"
            );
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDisplayName(request.getDisplayName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRegistrationType(request.getRegistrationType());
        user.setAccountStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        log.info(
                "User account created. userId={}, registrationType={}",
                savedUser.getId(),
                savedUser.getRegistrationType()
        );

        createProfileIfRequired(
                savedUser,
                request.getRegistrationType()
        );

        log.info(
                "User registration completed. userId={}, registrationType={}",
                savedUser.getId(),
                savedUser.getRegistrationType()
        );

        return successResponseBuilder.registerUser(
                savedUser.getId(),
                savedUser.getRegistrationType(),
                savedUser.getAccountStatus()
        );
    }

    private void createProfileIfRequired(
            User user,
            RegistrationType registrationType
    ) {

        switch (registrationType) {

            case USER -> log.debug(
                    "No performer profile required. userId={}",
                    user.getId()
            );

            case ARTIST -> {

                log.info(
                        "Creating artist profile. userId={}",
                        user.getId()
                );

                Artist artist = new Artist();
                artist.setUser(user);
                artist.setDisplayName(user.getDisplayName());

                artistRepository.save(artist);

                log.info(
                        "Artist profile created. userId={}, artistId={}",
                        user.getId(),
                        artist.getId()
                );
            }

            case BAND -> {

                log.info(
                        "Creating band profile. userId={}",
                        user.getId()
                );

                Band band = new Band();
                band.setOwner(user);
                band.setName(user.getEmail());

                bandRepository.save(band);

                log.info(
                        "Band profile created. userId={}, bandId={}",
                        user.getId(),
                        band.getId()
                );
            }
        }
    }
}