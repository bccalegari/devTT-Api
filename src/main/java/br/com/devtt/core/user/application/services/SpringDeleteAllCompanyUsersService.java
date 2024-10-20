package br.com.devtt.core.user.application.services;

import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.core.company.application.exceptions.CompanyNotFoundException;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.application.services.DeleteAllCompanyUsersService;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.DeleteOwnUserException;
import br.com.devtt.core.user.application.exceptions.DeleteStandardUserException;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway.UserRegistrationInvitationRepository;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.enterprise.application.exceptions.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringDeleteAllCompanyUsersService")
public class SpringDeleteAllCompanyUsersService implements DeleteAllCompanyUsersService<Integer> {
    private final CompanyRepository<CompanyEntity> companyRepository;
    private final UserRepository<UserEntity> userRepository;
    private final UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository;

    @Autowired
    public SpringDeleteAllCompanyUsersService(
            @Qualifier("HibernateCompanyRepository") CompanyRepository<CompanyEntity> companyRepository,
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("HibernateUserRegistrationInvitationRepository")
            UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userRegistrationInvitationRepository = userRegistrationInvitationRepository;
    }

    @Override
    public void execute(Integer idCompany, Long idLoggedUser) {
        var companyEntityOp = companyRepository.findById(idCompany);

        if (companyEntityOp.isEmpty()) {
            throw new CompanyNotFoundException("Empresa não encontrada");
        }

        var companyEntity = companyEntityOp.get();

        if (companyEntity.getId().equals(1)) {
            throw new DeleteStandardUserException("Você não pode deletar todos os usuários da empresa padrão");
        }

        var loggedUserEntityOp = userRepository.findById(idLoggedUser);

        if (loggedUserEntityOp.isEmpty()) {
            throw new CoreException();
        }

        var loggedUserEntity = loggedUserEntityOp.get();

        if (companyEntity.getId().equals(loggedUserEntity.getCompany().getId())) {
            throw new DeleteOwnUserException("Você não pode deletar todos os usuários da sua empresa");
        }

        userRegistrationInvitationRepository.disableAllRegistrationInvitationsByCompanyId(idCompany, idLoggedUser);
        userRepository.deleteByCompanyId(idCompany, idLoggedUser);
    }
}