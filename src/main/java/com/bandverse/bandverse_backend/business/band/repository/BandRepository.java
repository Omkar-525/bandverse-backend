package com.bandverse.bandverse_backend.business.band.repository;

import com.bandverse.bandverse_backend.business.band.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BandRepository extends JpaRepository<Band, UUID> {
}
