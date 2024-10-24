package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.GetAllUsersUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllUsersUseCaseValidatorServiceUnitTest {
    private final ValidatorService<GetAllUsersUseCaseValidatorDto> sut = new GetAllUsersUseCaseValidatorService();

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsEqualToSearchedUsersCompanyId() {
        var input = GetAllUsersUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUsersCompanyId(1)
                .build();

        var result = sut.execute(input);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsManagerAndSearchUsersCompanyIdIsNull() {
        var input = GetAllUsersUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUsersCompanyId(null)
                .build();

        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsNotEqualToSearchedUsersCompanyId() {
        var input = GetAllUsersUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUsersCompanyId(2)
                .build();

        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsMaster() {
        var input = GetAllUsersUseCaseValidatorDto.builder()
                .loggedUserRole("MASTER")
                .loggedUserCompanyId(1)
                .searchedUsersCompanyId(2)
                .build();

        var result = sut.execute(input);

        assertTrue(result);
    }
}