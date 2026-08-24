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

@Getter
@Setter
@Entity
@Table(name = "bands")
public class Band extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "owner_id",
            nullable = false
    )
    private User owner;

    @Column(
            name = "name",
            nullable = false,
            length = 255
    )
    private String name;
}
