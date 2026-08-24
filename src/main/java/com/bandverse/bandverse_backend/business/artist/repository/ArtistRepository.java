package com.bandverse.bandverse_backend.business.artist.repository;

import com.bandverse.bandverse_backend.business.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
}
