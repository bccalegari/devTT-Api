package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.adapters.gateway.database.repositories.UserRepository;
import br.com.devtt.core.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.abstractions.application.services.TokenService;
import br.com.devtt.core.abstractions.application.usecases.UserLoginUseCase;
import br.com.devtt.core.abstractions.domain.valueobjects.Token;
import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.application.exceptions.InvalidPasswordException;
import br.com.devtt.core.application.exceptions.UserNotFoundException;
import br.com.devtt.core.application.mappers.UserDomainMapper;
import br.com.devtt.core.domain.entities.User;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Qualifier("SpringUserLoginUseCase")
public class SpringUserLoginUseCase implements UserLoginUseCase {
    private TokenService tokenService;
    private PasswordEncoderService passwordEncoderService;
    private UserRepository userRepository;
    private DomainMapper<User, UserEntity> userDomainMapper;

    @Autowired
    public SpringUserLoginUseCase(@Qualifier("JwtTokenService") TokenService tokenService,
                                  @Qualifier("SpringBCryptPasswordEncoderService") PasswordEncoderService passwordEncoderService,
                                  @Qualifier("HibernateUserRepository") UserRepository userRepository) {
        this.tokenService = tokenService;
        this.passwordEncoderService = passwordEncoderService;
        this.userRepository = userRepository;
        this.userDomainMapper = UserDomainMapper.INSTANCE;
    }

    @Override
    public Token execute(String email, String password) {
        User user;
        var userEntity = userRepository.findByEmail(email);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("Email ou senha inválidos");
        }

        user = userDomainMapper.toDomain(userEntity.get());

        if (!passwordEncoderService.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Email ou senha inválidos");
        }

        return tokenService.create(user.getIdUser(), user.getFullName(), user.getRole().getName());
    }
}