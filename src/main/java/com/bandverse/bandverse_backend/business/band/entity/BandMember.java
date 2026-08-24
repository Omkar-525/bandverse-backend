package com.bandverse.bandverse_backend.business.band.entity;

import com.bandverse.bandverse_backend.business.user.entity.User;
import com.bandverse.bandverse_backend.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "band_members")
public class BandMember extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "band_id",
            nullable = false
    )
    private Band band;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            name = "joined_at",
            nullable = false
    )
    private OffsetDateTime joinedAt;

    @Column(
            name = "active",
            nullable = false
    )
    private boolean active = true;
}
