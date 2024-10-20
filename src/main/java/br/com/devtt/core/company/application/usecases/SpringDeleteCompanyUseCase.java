package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.DeleteCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.exceptions.DeleteOwnCompanyException;
import br.com.devtt.core.company.application.exceptions.DeleteStandardCompanyException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.application.services.DeleteAllCompanyUsersService;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.enterprise.application.exceptions.CoreException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringDeleteCompanyUseCase")
public class SpringDeleteCompanyUseCase implements DeleteCompanyUseCase {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final UserRepository<UserEntity> userRepository;
    private final DeleteAllCompanyUsersService<Integer> deleteAllCompanyUsersService;

    @Autowired
    public SpringDeleteCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("SpringDeleteAllCompanyUsersService") DeleteAllCompanyUsersService<Integer> deleteAllCompanyUsersService
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.deleteAllCompanyUsersService = deleteAllCompanyUsersService;
    }


    @Override
    @Transactional
    public void execute(Integer id, Long idLoggedUser) {
        var companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("A empresa não foi encontrada."));

        var loggedUser = userRepository.findById(idLoggedUser)
                .orElseThrow(CoreException::new);

        var loggedUserCompanyId = loggedUser.getCompany().getId();

        if (companyEntity.getId().equals(1)) {
            throw new DeleteStandardCompanyException("Você não tem permissão para deletar esta empresa.");
        }

        if (companyEntity.getId().equals(loggedUserCompanyId)) {
            throw new DeleteOwnCompanyException("Não é possível deletar sua própria empresa.");
        }

        deleteAllCompanyUsersService.execute(companyEntity.getId(), idLoggedUser);

        companyEntity.setUpdatedBy(idLoggedUser);
        companyEntity.setDeletedBy(idLoggedUser);

        companyRepository.delete(companyEntity);
    }
}