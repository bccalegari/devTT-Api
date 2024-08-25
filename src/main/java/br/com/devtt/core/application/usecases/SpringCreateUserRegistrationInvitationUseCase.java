package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.application.usecases.CreateUserRegistrationInvitationUseCase;
import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories.UserRegistrationInvitationRepository;
import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.application.mappers.UserRegistrationInvitationMapper;
import br.com.devtt.core.domain.entities.RegistrationInvitation;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringCreateUserRegistrationInvitationUseCase")
public class SpringCreateUserRegistrationInvitationUseCase
        implements CreateUserRegistrationInvitationUseCase<UserRegistrationInvitationEntity> {
    private final UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> repository;
    private final DomainMapper<RegistrationInvitation, UserRegistrationInvitationEntity> mapper;

    @Autowired
    public SpringCreateUserRegistrationInvitationUseCase(
            @Qualifier("HibernateUserRegistrationInvitationRepository")
            UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> repository,
            UserRegistrationInvitationMapper mapper

    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserRegistrationInvitationEntity create(Long idUser, String email, Long createdBy, Long idLoggedUser) {
        var registrationInvitation = repository.findByUserId(idUser);

        registrationInvitation.ifPresent(
                userRegistrationInvitationEntity ->
                        repository.disableRegistrationInvitation(userRegistrationInvitationEntity.getId(), idLoggedUser)
        );

        var userRegistrationInvitation = RegistrationInvitation.create(idUser, email, createdBy);
        var userRegistrationInvitationEntity = mapper.toEntity(userRegistrationInvitation);
        return repository.save(userRegistrationInvitationEntity);
    }
}