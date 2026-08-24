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
}
