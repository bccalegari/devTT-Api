package br.com.devtt.core.user.infrastructure.adapters.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GetAllUsersUseCaseValidatorDto {
    private Integer searchedUsersCompanyId;
    private String loggedUserRole;
    private Integer loggedUserCompanyId;
}