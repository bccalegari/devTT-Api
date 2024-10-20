package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.GetAllUsersUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetAllUsersOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Qualifier("SpringGetAllUsersUseCase")
@Service
public class SpringGetAllUsersUseCase implements GetAllUsersUseCase<GetAllUsersOutputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final DomainMapper<User, UserEntity> userMapper;
    private final AdapterMapper<User, GetUserOutputDto> adapterMapper;

    public SpringGetAllUsersUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            UserMapper userMapper, GetUserOutputDtoMapper adapterMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.adapterMapper = adapterMapper;
    }

    @Override
    public GetAllUsersOutputDto execute(Integer page, Integer size) {
        Page<UserEntity> userEntityPage;
        var paginationParams = new PaginationParams(page, size);

        userEntityPage = userRepository.findAll(paginationParams);

        if (userEntityPage.getContent().isEmpty()) {
            return new GetAllUsersOutputDto(
                    paginationParams.getCurrentPage(), paginationParams.getSize(), 0L, 0L, List.of()
            );
        }

        var userDomainList = userEntityPage.getContent().stream()
                .map(userMapper::toDomain)
                .toList();

        var usersDtoList = userDomainList.stream()
                .map(adapterMapper::toDto)
                .toList();

        return new GetAllUsersOutputDto(
                paginationParams.getCurrentPage(), paginationParams.getSize(),
                userEntityPage.getTotalElements(), userEntityPage.getTotalPages(), usersDtoList
        );
    }
}
