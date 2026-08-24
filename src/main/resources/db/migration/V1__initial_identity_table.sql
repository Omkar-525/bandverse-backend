CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(30),
    registration_type VARCHAR(20) NOT NULL,
    account_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL,

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_registration_type
        CHECK (registration_type IN ('USER', 'ARTIST', 'BAND')),
    CONSTRAINT chk_users_account_status
        CHECK (account_status IN ('ACTIVE', 'SUSPENDED', 'DEACTIVATED'))
);

CREATE TABLE artists (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    display_name VARCHAR(255) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL,

    CONSTRAINT uk_artists_user_id UNIQUE (user_id),

    CONSTRAINT fk_artists_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE bands (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL,

    CONSTRAINT fk_bands_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
);

CREATE TABLE band_members (
    id UUID PRIMARY KEY,
    band_id UUID NOT NULL,
    user_id UUID NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL,

    CONSTRAINT uk_band_members_band_user
        UNIQUE (band_id, user_id),

    CONSTRAINT fk_band_members_band
        FOREIGN KEY (band_id)
        REFERENCES bands(id),

    CONSTRAINT fk_band_members_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);