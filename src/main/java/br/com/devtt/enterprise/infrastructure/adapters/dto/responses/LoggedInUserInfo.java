package br.com.devtt.enterprise.infrastructure.adapters.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoggedInUserInfo {
    private final String name;
    private final String role;
}