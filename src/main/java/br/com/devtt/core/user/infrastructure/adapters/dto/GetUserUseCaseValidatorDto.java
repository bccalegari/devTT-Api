package br.com.devtt.core.user.infrastructure.adapters.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GetUserUseCaseValidatorDto {
    private Long searchedUserId;
    private Integer searchedUserCompanyId;
    private Long loggedUserId;
    private String loggedUserRole;
    private Integer loggedUserCompanyId;
}