package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.adapters.gateway.database.repositories.UserRegistrationInvitationRepository;
import br.com.devtt.core.abstractions.application.usecases.CreateUserRegistrationInvitationUseCase;
import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.application.mappers.UserRegistrationInvitationMapper;
import br.com.devtt.core.domain.entities.RegistrationInvitation;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringCreateUserRegistrationInvitationUseCase")
public class SpringCreateUserRegistrationInvitationUseCase implements CreateUserRegistrationInvitationUseCase {
    private UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> repository;
    private DomainMapper<RegistrationInvitation, UserRegistrationInvitationEntity> mapper;

    @Autowired
    public SpringCreateUserRegistrationInvitationUseCase(
            @Qualifier("HibernateUserRegistrationInvitationRepository")
            UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> repository
    ) {
        this.repository = repository;
        this.mapper = UserRegistrationInvitationMapper.INSTANCE;
    }

    @Override
    public void create(Long idUser, String email, Long createdBy, Long idLoggedUser) {
        var registrationInvitation = repository.findByUserId(idUser);

        registrationInvitation.ifPresent(
                userRegistrationInvitationEntity ->
                        repository.disableRegistrationInvitation(userRegistrationInvitationEntity.getId(), idLoggedUser)
        );

        var userRegistrationInvitation = RegistrationInvitation.create(idUser, email, createdBy);
        var userRegistrationInvitationEntity = mapper.toEntity(userRegistrationInvitation);
        repository.save(userRegistrationInvitationEntity);
    }
}