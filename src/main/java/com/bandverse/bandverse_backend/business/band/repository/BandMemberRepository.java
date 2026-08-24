package com.bandverse.bandverse_backend.business.band.repository;

import com.bandverse.bandverse_backend.business.band.entity.BandMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BandMemberRepository
        extends JpaRepository<BandMember, UUID> {
}
