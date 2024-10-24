package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.GetUserUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetUserUseCaseValidatorServiceUnitTest {
    private final ValidatorService<GetUserUseCaseValidatorDto> sut = new GetUserUseCaseValidatorService();

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsUserAndLoggedUserIdIsEqualToSearchedUserId() {
        var input = GetUserUseCaseValidatorDto.builder()
                .loggedUserRole("USER")
                .loggedUserId(1L)
                .searchedUserId(1L)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsUserAndLoggedUserIdIsDifferentFromSearchedUserId() {
        var input = GetUserUseCaseValidatorDto.builder()
                .loggedUserRole("USER")
                .loggedUserId(1L)
                .searchedUserId(2L)
                .build();
        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsEqualToSearchedUserCompanyId() {
        var input = GetUserUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(1)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsDifferentFromSearchedUserCompanyId() {
        var input = GetUserUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(2)
                .build();
        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsMaster() {
        var input = GetUserUseCaseValidatorDto.builder()
                .loggedUserRole("MASTER")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(2)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }
}