package com.bandverse.bandverse_backend.business.artist.entity;

import com.bandverse.bandverse_backend.business.user.entity.User;
import com.bandverse.bandverse_backend.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "artists")
public class Artist extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(
            name = "display_name",
            nullable = false,
            length = 255
    )
    private String displayName;
}
