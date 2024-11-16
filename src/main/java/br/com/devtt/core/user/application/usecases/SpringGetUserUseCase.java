package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.GetUserUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.dto.GetUserUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.application.exceptions.InsufficientCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier("SpringGetUserUseCase")
@Service
public class SpringGetUserUseCase implements GetUserUseCase<Long, GetUserOutputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final ValidatorService<GetUserUseCaseValidatorDto> validatorService;
    private final CacheGateway cacheGateway;
    private final DomainMapper<User, UserEntity> userMapper;
    private final AdapterMapper<User, GetUserOutputDto> adapterMapper;
    private final ObjectMapper objectMapper;

    public SpringGetUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("GetUserUseCaseValidatorService") ValidatorService<GetUserUseCaseValidatorDto> validatorService,
            @Qualifier("RedisCacheGateway") CacheGateway cacheGateway,
            UserMapper userMapper, GetUserOutputDtoMapper adapterMapper, ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.validatorService = validatorService;
        this.cacheGateway = cacheGateway;
        this.userMapper = userMapper;
        this.adapterMapper = adapterMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public GetUserOutputDto execute(Long idUser, Long loggedUserId, String loggedUserRole, Integer loggedUserCompanyId) {
        var userFromCache = cacheGateway.get(UserCacheKeys.USER.getKey().formatted(idUser));

        if (userFromCache != null) {
            return objectMapper.convertValue(userFromCache, GetUserOutputDto.class);
        }

        var userEntityOp = userRepository.findById(idUser);

        if (userEntityOp.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        var userEntity = userEntityOp.get();

        var validatorDto = buildValidatorDto(
                idUser, userEntity.getCompany().getId(), loggedUserId, loggedUserRole, loggedUserCompanyId
        );

        if (!validatorService.execute(validatorDto)) {
            throw new InsufficientCredentialsException();
        }

        var user = userMapper.toDomain(userEntity);

        var userOutputDto = adapterMapper.toDto(user);

        cacheGateway.put(UserCacheKeys.USER.getKey().formatted(idUser), userOutputDto);

        return userOutputDto;
    }

    private GetUserUseCaseValidatorDto buildValidatorDto(
            Long idUser, Integer searchedUserCompanyId, Long loggedUserId,
            String loggedUserRole, Integer loggedUserCompanyId
    ) {
        return GetUserUseCaseValidatorDto.builder()
                .searchedUserId(idUser)
                .searchedUserCompanyId(searchedUserCompanyId)
                .loggedUserId(loggedUserId)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();
    }
}
