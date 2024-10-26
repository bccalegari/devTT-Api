package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.UpdateUserUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUserUseCaseValidatorServiceUnitTest {
    private final ValidatorService<UpdateUserUseCaseValidatorDto> sut = new UpdateUserUseCaseValidatorService();

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsUserAndLoggedUserIdIsEqualToSearchedUserId() {
        var input = UpdateUserUseCaseValidatorDto.builder()
                .loggedUserRole("USER")
                .loggedUserId(1L)
                .searchedUserId(1L)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsUserAndLoggedUserIdIsDifferentFromSearchedUserId() {
        var input = UpdateUserUseCaseValidatorDto.builder()
                .loggedUserRole("USER")
                .loggedUserId(1L)
                .searchedUserId(2L)
                .build();
        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsEqualToSearchedUserCompanyId() {
        var input = UpdateUserUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(1)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLoggedUserRoleIsManagerAndLoggedUserCompanyIdIsDifferentFromSearchedUserCompanyId() {
        var input = UpdateUserUseCaseValidatorDto.builder()
                .loggedUserRole("Manager")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(2)
                .build();
        var result = sut.execute(input);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenLoggedUserRoleIsMaster() {
        var input = UpdateUserUseCaseValidatorDto.builder()
                .loggedUserRole("MASTER")
                .loggedUserCompanyId(1)
                .searchedUserCompanyId(2)
                .build();
        var result = sut.execute(input);

        assertTrue(result);
    }
}