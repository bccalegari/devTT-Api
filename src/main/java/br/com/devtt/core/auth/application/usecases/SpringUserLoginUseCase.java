package br.com.devtt.core.auth.application.usecases;

import br.com.devtt.core.auth.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.auth.abstractions.application.services.TokenService;
import br.com.devtt.core.auth.abstractions.application.usecases.UserLoginUseCase;
import br.com.devtt.core.auth.abstractions.domain.valueobjects.Token;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.auth.application.exceptions.InvalidPasswordException;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("SpringUserLoginUseCase")
public class SpringUserLoginUseCase implements UserLoginUseCase {
    private final TokenService tokenService;
    private final PasswordEncoderService passwordEncoderService;
    private final UserRepository<UserEntity> repository;
    private final DomainMapper<User, UserEntity> mapper;

    @Autowired
    public SpringUserLoginUseCase(
            @Qualifier("JwtTokenService") TokenService tokenService,
            @Qualifier("SpringBCryptPasswordEncoderService") PasswordEncoderService passwordEncoderService,
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            UserMapper userMapper
    ) {
        this.tokenService = tokenService;
        this.passwordEncoderService = passwordEncoderService;
        repository = userRepository;
        mapper = userMapper;
    }

    @Override
    public Token execute(String email, String password) {
        var userEntity = repository.findByEmail(email);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("Email ou senha inválidos");
        }

        var user = mapper.toDomain(userEntity.get());

        if (!passwordEncoderService.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Email ou senha inválidos");
        }

        return tokenService.create(user.getId(), user.getFullName(), user.getRole().getName(), user.getCompany().getId());
    }
}