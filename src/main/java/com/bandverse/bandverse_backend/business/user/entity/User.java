package com.bandverse.bandverse_backend.business.user.entity;

import com.bandverse.bandverse_backend.util.enums.AccountStatus;
import com.bandverse.bandverse_backend.util.enums.RegistrationType;
import lombok.Getter;
import lombok.Setter;
import com.bandverse.bandverse_backend.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    @Column(
            name = "phone",
            length = 30
    )
    private String phone;

    @Column(
            name = "password_hash",
            length = 255
    )
    private String passwordHash;

    @Column(
            name = "display_name",
            length = 255
    )
    private String displayName;

    @Column(
            name = "avatar_url",
            length = 500
    )
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "registration_type",
            nullable = false,
            length = 20
    )
    private RegistrationType registrationType;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "account_status",
            nullable = false,
            length = 20
    )
    private AccountStatus accountStatus;

    @Column(
            name = "email_verified_at"
    )
    private OffsetDateTime emailVerifiedAt;

    @Column(
            name = "phone_verified_at"
    )
    private OffsetDateTime phoneVerifiedAt;

    @Column(
            name = "mfa_enabled",
            nullable = false
    )
    private boolean mfaEnabled;

    @Column(
            name = "deleted_at"
    )
    private OffsetDateTime deletedAt;
}