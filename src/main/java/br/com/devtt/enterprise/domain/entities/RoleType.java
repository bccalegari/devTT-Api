package br.com.devtt.enterprise.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    MASTER("Master"),
    MANAGER("Manager"),
    USER("User");

    private final String value;

    public static RoleType fromString(String role) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getValue().equalsIgnoreCase(role)) {
                return roleType;
            }
        }
        return null;
    }
}