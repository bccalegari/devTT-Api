package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.GetUserUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.UpdateUserUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.domain.entities.RoleType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("UpdateUserUseCaseValidatorService")
public class UpdateUserUseCaseValidatorService implements ValidatorService<UpdateUserUseCaseValidatorDto> {
    @Override
    public boolean execute(UpdateUserUseCaseValidatorDto input) {
        var isUser = input.getLoggedUserRole().equalsIgnoreCase(RoleType.USER.getValue());
        var isManager = input.getLoggedUserRole().equalsIgnoreCase(RoleType.MANAGER.getValue());

        if (isUser) {
            return validateWhenLoggedUserRoleIsUser(input);
        } else if (isManager) {
            return validateWhenLoggedUserRoleIsManager(input);
        }

        return true;
    }

    private boolean validateWhenLoggedUserRoleIsUser(UpdateUserUseCaseValidatorDto input) {
        return input.getLoggedUserId().equals(input.getSearchedUserId());
    }

    private boolean validateWhenLoggedUserRoleIsManager(UpdateUserUseCaseValidatorDto input) {
        return input.getLoggedUserCompanyId().equals(input.getSearchedUserCompanyId());
    }
}