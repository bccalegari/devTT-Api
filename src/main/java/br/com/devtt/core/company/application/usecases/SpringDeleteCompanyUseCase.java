package br.com.devtt.core.company.application.usecases;

import br.com.devtt.core.company.abstractions.application.usecases.DeleteCompanyUseCase;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.application.exceptions.DeleteOwnCompanyException;
import br.com.devtt.core.company.application.exceptions.DeleteStandardCompanyException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
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

    @Autowired
    public SpringDeleteCompanyUseCase(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void execute(Integer id, Long idLoggedUser) {
        if (id == 1) {
            throw new DeleteStandardCompanyException("Você não tem permissão para deletar essa empresa.");
        }

        var companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("A empresa não foi encontrada."));

        var loggedUser = userRepository.findById(idLoggedUser)
                .orElseThrow(CoreException::new);

        var loggedUserCompanyId = loggedUser.getCompany().getId();

        if (companyEntity.getId().equals(loggedUserCompanyId)) {
            throw new DeleteOwnCompanyException("Não é possível deletar sua própria empresa.");
        }

        companyEntity.setUpdatedBy(idLoggedUser);
        companyEntity.setDeletedBy(idLoggedUser);

        companyRepository.delete(companyEntity);
    }
}