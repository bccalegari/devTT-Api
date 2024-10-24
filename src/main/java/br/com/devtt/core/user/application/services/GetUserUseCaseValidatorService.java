package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.GetUserUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.domain.entities.RoleType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("GetUserUseCaseValidatorService")
public class GetUserUseCaseValidatorService implements ValidatorService<GetUserUseCaseValidatorDto> {
    @Override
    public boolean validate(GetUserUseCaseValidatorDto input) {
        var isUser = input.getLoggedUserRole().equalsIgnoreCase(RoleType.USER.getValue());
        var isAdminOrManager = input.getLoggedUserRole().equalsIgnoreCase(RoleType.ADMIN.getValue())
                || input.getLoggedUserRole().equalsIgnoreCase(RoleType.MANAGER.getValue());

        if (isUser) {
            return validateWhenLoggedUserRoleIsUser(input);
        } else if (isAdminOrManager) {
            return validateWhenLoggedUserRoleIsAdminOrManager(input);
        }

        return true;
    }

    private boolean validateWhenLoggedUserRoleIsUser(GetUserUseCaseValidatorDto input) {
        return input.getLoggedUserId().equals(input.getSearchedUserId());
    }

    private boolean validateWhenLoggedUserRoleIsAdminOrManager(GetUserUseCaseValidatorDto input) {
        return input.getLoggedUserCompanyId().equals(input.getSearchedUserCompanyId());
    }
}