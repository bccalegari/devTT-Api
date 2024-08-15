package br.com.devtt.core.application.usecases;

import br.com.devtt.core.abstractions.adapters.gateway.database.repositories.UserRepository;
import br.com.devtt.core.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.abstractions.application.usecases.CreateUserRegistrationInvitationUseCase;
import br.com.devtt.core.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.core.abstractions.mappers.AdapterMapper;
import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.application.mappers.UserMapper;
import br.com.devtt.core.domain.entities.User;
import br.com.devtt.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.infrastructure.adapters.mappers.CreateUserInputDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("SpringCreateUserUseCase")
public class SpringCreateUserUseCase implements CreateUserUseCase<CreateUserInputDto> {
    private UserRepository<UserEntity> userRepository;
    private PasswordEncoderService passwordEncoderService;
    private CreateUserRegistrationInvitationUseCase createUserRegistrationInvitationUseCase;
    private AdapterMapper<User, CreateUserInputDto> adapterMapper;
    private DomainMapper<User, UserEntity> domainMapper;

    @Autowired
    public SpringCreateUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("SpringBCryptPasswordEncoderService") PasswordEncoderService passwordEncoderService,
            @Qualifier("SpringCreateUserRegistrationInvitationUseCase")
            CreateUserRegistrationInvitationUseCase createUserRegistrationInvitationUseCase
    ) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.createUserRegistrationInvitationUseCase = createUserRegistrationInvitationUseCase;
        adapterMapper = CreateUserInputDtoMapper.INSTANCE;
        domainMapper = UserMapper.INSTANCE;
    }

    @Override
    @Transactional
    public void create(CreateUserInputDto input, Long idLoggedUser) {
        var userEntityOnDatabase = userRepository.findByPhoneOrEmailOrCpf(
                Long.parseLong(input.getPhone()), input.getEmail(), input.getCpf()
        );

        if (userEntityOnDatabase.isPresent()) {
            throw new RuntimeException();
        }

        var user = adapterMapper.toDomain(input);
        user.setPassword(passwordEncoderService.encode(user.getPassword()));

        var userEntity = domainMapper.toEntity(user);
        userEntity.setCreatedBy(idLoggedUser);
        userEntity = userRepository.save(userEntity);

        createUserRegistrationInvitationUseCase.create(userEntity.getId(), userEntity.getEmail(),
                userEntity.getCreatedBy(), idLoggedUser);

    }
}