package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.DeleteUserUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.DeleteOwnUserException;
import br.com.devtt.core.user.application.exceptions.DeleteStandardUserException;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway.UserRegistrationInvitationRepository;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("SpringDeleteUserUseCase")
public class SpringDeleteUserUseCase implements DeleteUserUseCase<Long> {
    private final UserRepository<UserEntity> userRepository;
    private final UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository;
    private final CacheGateway cacheGateway;

    @Autowired
    public SpringDeleteUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("HibernateUserRegistrationInvitationRepository")
            UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> userRegistrationInvitationRepository,
            @Qualifier("RedisCacheGateway") CacheGateway cacheGateway

    ) {
        this.userRepository = userRepository;
        this.userRegistrationInvitationRepository = userRegistrationInvitationRepository;
        this.cacheGateway = cacheGateway;
    }

    @Override
    @Transactional
    public void execute(Long idUser, Long idLoggedUser) {
        var userEntityOp = userRepository.findById(idUser);

        if (userEntityOp.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        var userEntity = userEntityOp.get();

        if (userEntity.getId().equals(idLoggedUser)) {
            throw new DeleteOwnUserException("Não é possível deletar o próprio usuário");
        }

        if(userEntity.getId().equals(1L)) {
            throw new DeleteStandardUserException("Você não tem permissão para deletar este usuário");
        }

        userEntity.setUpdatedBy(idLoggedUser);
        userEntity.setDeletedBy(idLoggedUser);

        userRegistrationInvitationRepository.disableAllRegistrationInvitationsByUserId(idUser, idLoggedUser);
        userRepository.delete(userEntity);

        cacheGateway.delete(UserCacheKeys.USER.getKey().formatted(idUser));
        cacheGateway.deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());
    }
}