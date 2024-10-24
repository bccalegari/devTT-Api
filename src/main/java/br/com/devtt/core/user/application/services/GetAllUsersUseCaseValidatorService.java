package br.com.devtt.core.user.application.services;

import br.com.devtt.core.user.infrastructure.adapters.dto.GetAllUsersUseCaseValidatorDto;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.domain.entities.RoleType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("GetAllUsersUseCaseValidatorService")
public class GetAllUsersUseCaseValidatorService implements ValidatorService<GetAllUsersUseCaseValidatorDto> {
    @Override
    public boolean validate(GetAllUsersUseCaseValidatorDto input) {
        var isAdminOrManager = input.getLoggedUserRole().equalsIgnoreCase(RoleType.ADMIN.getValue())
                || input.getLoggedUserRole().equalsIgnoreCase(RoleType.MANAGER.getValue());

        if (isAdminOrManager) {
            return validateWhenLoggedUserRoleIsAdminOrManager(input);
        }

        return true;
    }

    private boolean validateWhenLoggedUserRoleIsAdminOrManager(GetAllUsersUseCaseValidatorDto input) {
        if (input.getSearchedUsersCompanyId() == null) {
            return false;
        }

        return input.getLoggedUserCompanyId().equals(input.getSearchedUsersCompanyId());
    }
}