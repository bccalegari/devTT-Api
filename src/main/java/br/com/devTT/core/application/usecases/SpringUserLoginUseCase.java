package br.com.devTT.core.application.usecases;

import br.com.devTT.core.abstractions.adapters.gateway.database.repositories.UserRepository;
import br.com.devTT.core.abstractions.application.services.PasswordEncoderService;
import br.com.devTT.core.abstractions.application.services.TokenService;
import br.com.devTT.core.abstractions.application.usecases.UserLoginUseCase;
import br.com.devTT.core.abstractions.domain.valueobjects.Token;
import br.com.devTT.core.abstractions.mappers.DomainMapper;
import br.com.devTT.core.application.exceptions.InvalidPasswordException;
import br.com.devTT.core.application.exceptions.UserNotFoundException;
import br.com.devTT.core.application.mappers.UserDomainMapper;
import br.com.devTT.core.domain.entities.User;
import br.com.devTT.infrastructure.adapters.gateway.database.entities.UserEntity;
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
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        user = userDomainMapper.toDomain(userEntity.get());

        if (!passwordEncoderService.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }

        return tokenService.create(1L);
    }
}