package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.GetAllUsersUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.GetAllUsersUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetAllUsersOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.application.exceptions.InsufficientCredentialsException;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Qualifier("SpringGetAllUsersUseCase")
@Service
public class SpringGetAllUsersUseCase implements GetAllUsersUseCase<GetAllUsersOutputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final ValidatorService<GetAllUsersUseCaseValidatorDto> validatorService;
    private final CacheGateway cacheGateway;
    private final DomainMapper<User, UserEntity> userMapper;
    private final AdapterMapper<User, GetUserOutputDto> adapterMapper;
    private final ObjectMapper objectMapper;

    public SpringGetAllUsersUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("GetAllUsersUseCaseValidatorService") ValidatorService<GetAllUsersUseCaseValidatorDto> validatorService,
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
    public GetAllUsersOutputDto execute(
            Integer page, Integer size, Integer idCompany, String search,
            String loggedUserRole, Integer loggedUserCompanyId
    ) {
        Page<UserEntity> userEntityPage;
        var paginationParams = new PaginationParams(page, size);

        var usersFromCache = cacheGateway
                .get(UserCacheKeys.USERS_PAGED.getKey().formatted(page, size, idCompany, search));

        if (usersFromCache != null) {
            return objectMapper.convertValue(usersFromCache, GetAllUsersOutputDto.class);
        }

        userEntityPage = userRepository.findAll(paginationParams, search, idCompany);

        var validatorDto = buildValidatorDto(idCompany, loggedUserRole, loggedUserCompanyId);

        if (!validatorService.execute(validatorDto)) {
            throw new InsufficientCredentialsException();
        }

        if (userEntityPage.getContent().isEmpty()) {
            return GetAllUsersOutputDto.builder()
                    .currentPage(paginationParams.getCurrentPage())
                    .size(paginationParams.getSize())
                    .totalElements(0L)
                    .totalPages(0L)
                    .users(List.of())
                    .build();
        }

        var userDomainList = userEntityPage.getContent().stream()
                .map(userMapper::toDomain)
                .toList();

        var usersDtoList = userDomainList.stream()
                .map(adapterMapper::toDto)
                .toList();

        var usersOutputDto = GetAllUsersOutputDto.builder()
                .currentPage(paginationParams.getCurrentPage())
                .size(paginationParams.getSize())
                .totalElements(userEntityPage.getTotalElements())
                .totalPages(userEntityPage.getTotalPages())
                .users(usersDtoList)
                .build();

        cacheGateway.put(UserCacheKeys.USERS_PAGED.getKey().formatted(page, size, idCompany, search), usersOutputDto);

        return usersOutputDto;
    }

    private GetAllUsersUseCaseValidatorDto buildValidatorDto(Integer idCompany, String loggedUserRole, Integer loggedUserCompanyId) {
        return GetAllUsersUseCaseValidatorDto.builder()
                .searchedUsersCompanyId(idCompany)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();
    }
}
