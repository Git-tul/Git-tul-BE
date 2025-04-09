package io.gittul.core.domain.user.entity;

import java.util.Collections;
import java.util.List;

public enum Role {
    USER,
    MODERATOR(List.of(USER)),
    ADMIN(List.of(USER, MODERATOR));

    private final List<Role> includedRoles;

    Role() {
        this.includedRoles = Collections.emptyList();
    }

    Role(List<Role> includedRoles) {
        this.includedRoles = includedRoles;
    }

    public boolean includes(Role required) {
        if (this == required) {
            return true;
        }
        return includedRoles.contains(required);
    }
}
