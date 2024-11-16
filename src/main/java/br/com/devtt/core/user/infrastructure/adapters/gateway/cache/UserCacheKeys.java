package br.com.devtt.core.user.infrastructure.adapters.gateway.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserCacheKeys {
    USER_PATTERN("user"),
    USER("user::id=%s"),
    USERS_PATTERN("users"),
    USERS_PAGED("users::page=%s&size=%s&idCompany=%s&search=%s");

    private final String key;
}