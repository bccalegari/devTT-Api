package br.com.devtt.core.user.application.usecases;

import br.com.devtt.enterprise.abstractions.application.services.MailService;
import br.com.devtt.core.auth.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.user.invitation.abstractions.application.usecases.CreateUserRegistrationInvitationUseCase;
import br.com.devtt.core.user.abstractions.application.usecases.CreateUserUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.user.application.exceptions.UserAlreadyExistsException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.invitation.infrastructure.adapters.dto.UserInvitationEmailPayload;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.CreateUserInputDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("SpringCreateUserUseCase")
public class SpringCreateUserUseCase implements CreateUserUseCase<CreateUserInputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final CreateUserRegistrationInvitationUseCase<UserRegistrationInvitationEntity>
            createUserRegistrationInvitationUseCase;
    private final MailService<UserInvitationEmailPayload> mailService;
    private final AdapterMapper<User, CreateUserInputDto> adapterMapper;
    private final DomainMapper<User, UserEntity> domainMapper;

    @Autowired
    public SpringCreateUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("SpringBCryptPasswordEncoderService") PasswordEncoderService passwordEncoderService,
            @Qualifier("SpringCreateUserRegistrationInvitationUseCase")
            CreateUserRegistrationInvitationUseCase<UserRegistrationInvitationEntity> createUserRegistrationInvitationUseCase,
            @Qualifier("RabbitInvitationMailProducerService") MailService<UserInvitationEmailPayload> mailService,
            CreateUserInputDtoMapper createUserInputDtoMapper, UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.createUserRegistrationInvitationUseCase = createUserRegistrationInvitationUseCase;
        this.mailService = mailService;
        adapterMapper = createUserInputDtoMapper;
        domainMapper = userMapper;
    }

    @Override
    @Transactional
    public void execute(CreateUserInputDto input, Long idLoggedUser, String loggedUserName) {
        var userEntityOnDatabase = userRepository.findByPhoneOrEmailOrCpf(
                Long.parseLong(input.getPhone()), input.getEmail(), input.getCpf()
        );

        if (userEntityOnDatabase.isPresent()) {
            throw new UserAlreadyExistsException(" Já existe um usuário com o mesmo CPF, telefone " +
                    "ou e-mail cadastrado no sistema. Verifique os dados e tente novamente.");
        }

        var user = adapterMapper.toDomain(input);
        user.setPassword(passwordEncoderService.encode(user.getPassword()));

        var userEntity = domainMapper.toEntity(user);
        userEntity.setCreatedBy(idLoggedUser);
        userEntity = userRepository.save(userEntity);

        var userInvitationEntity = createUserRegistrationInvitationUseCase.execute(userEntity.getId(),
                userEntity.getEmail(), userEntity.getCreatedBy(), idLoggedUser);

        sendEmail(user, userInvitationEntity.getToken(), loggedUserName);
    }

    private void sendEmail(User user, String token, String creatorName) {
        var payload = new UserInvitationEmailPayload(user.getFullName(), user.getEmail(), token, creatorName);
        mailService.send(payload);
    }
}