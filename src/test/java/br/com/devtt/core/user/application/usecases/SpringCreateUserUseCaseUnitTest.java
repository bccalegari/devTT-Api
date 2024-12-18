package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.auth.abstractions.application.services.PasswordEncoderService;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserAlreadyExistsException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.domain.valueobjects.Cpf;
import br.com.devtt.core.user.domain.valueobjects.Sex;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.CreateUserInputDtoMapper;
import br.com.devtt.core.user.invitation.abstractions.application.usecases.CreateUserRegistrationInvitationUseCase;
import br.com.devtt.core.user.invitation.infrastructure.adapters.dto.UserInvitationEmailPayload;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import br.com.devtt.enterprise.abstractions.application.services.MailService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.domain.entities.City;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.enterprise.domain.valueobjects.Address;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.enterprise.domain.valueobjects.Cep;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.CityEntity;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpringCreateUserUseCaseUnitTest {
    @InjectMocks private SpringCreateUserUseCase useCase;
    @Mock private UserRepository<UserEntity> userRepository;
    @Mock private PasswordEncoderService passwordEncoderService;
    @Mock private CreateUserRegistrationInvitationUseCase<UserRegistrationInvitationEntity> createUserRegistrationInvitationUseCase;
    @Mock private MailService<UserInvitationEmailPayload> mailService;
    @Mock private CacheGateway cacheGateway;
    @Mock private CreateUserInputDtoMapper createUserInputDtoMapper;
    @Mock private UserMapper userMapper;
    private CreateUserInputDto inputDto;
    private Long idLoggedUser;
    private String loggedUserName;
    private User userDomain;
    private UserEntity userEntity;
    private UserRegistrationInvitationEntity userRegistrationInvitationEntity;

    @BeforeEach
    void setUp() {
        inputDto = CreateUserInputDto.builder()
                .name("name")
                .lastName("lastName")
                .phone("123456789")
                .email("email")
                .password("password")
                .cpf("45488723432")
                .birthDate(LocalDate.now())
                .sex("M")
                .street("street")
                .streetNumber(1)
                .district("district")
                .cep("23456423")
                .idCity(1L)
                .idRole(1)
                .idCompany(1)
                .build();
        idLoggedUser = 1L;
        loggedUserName = "loggedUserName";
        userDomain = User.builder()
                .name(inputDto.getName())
                .lastName(inputDto.getLastName())
                .phone(Long.parseLong(inputDto.getPhone()))
                .email(inputDto.getEmail())
                .password(inputDto.getPassword())
                .cpf(new Cpf(inputDto.getCpf()))
                .birthDate(inputDto.getBirthDate())
                .sex(Sex.fromCode(inputDto.getSex()))
                .address(Address.builder()
                        .street(inputDto.getStreet())
                        .streetNumber(inputDto.getStreetNumber())
                        .district(inputDto.getDistrict())
                        .cep(new Cep(inputDto.getCep()))
                        .city(City.builder().id(inputDto.getIdCity()).build())
                        .build())
                .auditing(Auditing.builder().createdBy(idLoggedUser).build())
                .role(Role.builder().id(inputDto.getIdRole()).build())
                .company(Company.builder().id(inputDto.getIdCompany()).build())
                .build();
        userEntity = UserEntity.builder()
                .name(userDomain.getName())
                .lastName(userDomain.getLastName())
                .phone(userDomain.getPhone())
                .email(userDomain.getEmail())
                .password(userDomain.getPassword())
                .cpf(userDomain.getCpf().getValue())
                .birthDate(userDomain.getBirthDate())
                .sex(userDomain.getSex().getCode())
                .street(userDomain.getAddress().getStreet())
                .streetNumber(userDomain.getAddress().getStreetNumber())
                .district(userDomain.getAddress().getDistrict())
                .cep(userDomain.getAddress().getCep().getValue())
                .city(CityEntity.builder().id(userDomain.getAddress().getCity().getId()).build())
                .role(RoleEntity.builder().id(userDomain.getRole().getId()).build())
                .company(CompanyEntity.builder().id(userDomain.getCompany().getId()).build())
                .createdBy(userDomain.getAuditing().getCreatedBy())
                .build();
        userRegistrationInvitationEntity = UserRegistrationInvitationEntity.builder()
                .email(userEntity.getEmail())
                .user(userEntity)
                .createdBy(userEntity.getCreatedBy())
                .expirationDt(LocalDateTime.now().plusDays(7))
                .token("token")
                .build();

    }

    @Test
    void shouldCreateUser() {
        when(userRepository.findByPhoneOrEmailOrCpf(anyLong(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(createUserInputDtoMapper.toDomain(inputDto)).thenReturn(userDomain);
        when(passwordEncoderService.encode(userDomain.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toEntity(userDomain)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(createUserRegistrationInvitationUseCase.execute(
                userEntity.getId(), userEntity.getEmail(), userEntity.getCreatedBy(), idLoggedUser
                ))
                .thenReturn(userRegistrationInvitationEntity);
        doNothing().when(mailService).send(any(UserInvitationEmailPayload.class));
        doNothing().when(cacheGateway).deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());

        useCase.execute(inputDto, idLoggedUser, loggedUserName);

        verify(userRepository).findByPhoneOrEmailOrCpf(
                Long.parseLong(inputDto.getPhone()), inputDto.getEmail(), inputDto.getCpf()
        );
        verify(createUserInputDtoMapper).toDomain(inputDto);
        verify(passwordEncoderService).encode(inputDto.getPassword());
        verify(userMapper).toEntity(userDomain);
        verify(userRepository).save(userEntity);
        verify(createUserRegistrationInvitationUseCase).execute(
                userEntity.getId(), userEntity.getEmail(), userEntity.getCreatedBy(), idLoggedUser
        );
        verify(mailService).send(new UserInvitationEmailPayload(
                userDomain.getFullName(), userDomain.getEmail(), userRegistrationInvitationEntity.getToken(), loggedUserName
        ));
        verify(cacheGateway).deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());
        verifyNoMoreInteractions(userRepository, createUserInputDtoMapper, passwordEncoderService,
                userMapper, mailService, cacheGateway);
    }

    @Test
    void shouldThrowUserAlreadyExistsException() {
        when(userRepository.findByPhoneOrEmailOrCpf(anyLong(), anyString(), anyString()))
                .thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsException.class, () -> useCase.execute(inputDto, idLoggedUser, loggedUserName));

        verify(userRepository).findByPhoneOrEmailOrCpf(
                Long.parseLong(inputDto.getPhone()), inputDto.getEmail(), inputDto.getCpf()
        );

        verifyNoInteractions(createUserInputDtoMapper, passwordEncoderService, userMapper, mailService, cacheGateway);
        verifyNoMoreInteractions(userRepository);
    }
}