package com.bandverse.bandverse_backend.security;

import com.bandverse.bandverse_backend.business.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.bandverse.bandverse_backend.util.enums.AccountStatus;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsModel implements UserDetails {

    private final User user;

    public UserDetailsModel(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getAccountStatus() == AccountStatus.ACTIVE
                && user.getDeletedAt() == null;
    }

    public User getUser() {
        return user;
    }
}