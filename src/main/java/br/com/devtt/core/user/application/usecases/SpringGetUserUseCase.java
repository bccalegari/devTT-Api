package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.GetUserUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier("SpringGetUserUseCase")
@Service
public class SpringGetUserUseCase implements GetUserUseCase<Long, GetUserOutputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final DomainMapper<User, UserEntity> userMapper;
    private final AdapterMapper<User, GetUserOutputDto> adapterMapper;

    public SpringGetUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            UserMapper userMapper, GetUserOutputDtoMapper adapterMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.adapterMapper = adapterMapper;
    }

    @Override
    public GetUserOutputDto execute(Long idUser) {
        var userEntityOp = userRepository.findById(idUser);

        if (userEntityOp.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado.");
        }

        var userEntity = userEntityOp.get();

        var user = userMapper.toDomain(userEntity);

        return adapterMapper.toDto(user);
    }
}
