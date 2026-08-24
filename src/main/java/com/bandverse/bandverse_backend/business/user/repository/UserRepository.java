package com.bandverse.bandverse_backend.business.user.repository;

import com.bandverse.bandverse_backend.business.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
}
