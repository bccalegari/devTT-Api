package br.com.devtt.core.user.application.usecases;

import br.com.devtt.core.user.abstractions.application.usecases.UpdateUserUseCase;
import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.application.exceptions.UserAlreadyExistsException;
import br.com.devtt.core.user.application.exceptions.UserNotFoundException;
import br.com.devtt.core.user.application.mappers.UserMapper;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.gateway.cache.UserCacheKeys;
import br.com.devtt.core.user.infrastructure.adapters.dto.UpdateUserUseCaseValidatorDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.UpdateUserInputDto;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.infrastructure.adapters.mappers.GetUserOutputDtoMapper;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import br.com.devtt.enterprise.abstractions.application.services.ValidatorService;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.CacheGateway;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.application.exceptions.InsufficientCredentialsException;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.CityEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringUpdateUserUseCase")
public class SpringUpdateUserUseCase implements UpdateUserUseCase<UpdateUserInputDto> {
    private final UserRepository<UserEntity> userRepository;
    private final ValidatorService<UpdateUserUseCaseValidatorDto> validatorService;
    private final ComparatorService comparatorService;
    private final CacheGateway cacheGateway;
    private final DomainMapper<User, UserEntity> domainMapper;
    private final AdapterMapper<User, GetUserOutputDto> adapterMapper;

    public SpringUpdateUserUseCase(
            @Qualifier("HibernateUserRepository") UserRepository<UserEntity> userRepository,
            @Qualifier("UpdateUserUseCaseValidatorService") ValidatorService<UpdateUserUseCaseValidatorDto> validatorService,
            @Qualifier("ComparatorServiceImpl") ComparatorService comparatorService,
            @Qualifier("RedisCacheGateway") CacheGateway cacheGateway,
            UserMapper domainMapper, GetUserOutputDtoMapper adapterMapper
    ) {
        this.userRepository = userRepository;
        this.validatorService = validatorService;
        this.comparatorService = comparatorService;
        this.cacheGateway = cacheGateway;
        this.domainMapper = domainMapper;
        this.adapterMapper = adapterMapper;
    }

    @Override
    @Transactional
    public void execute(
            UpdateUserInputDto input, Long userToBeUpdatedId, Long idLoggedUser,
            String loggedUserRole, Integer loggedUserCompanyId
    ) {
        var userEntityOp = userRepository.findById(userToBeUpdatedId);

        if (userEntityOp.isEmpty()) {
            throw new UserNotFoundException("O usuário não foi encontrado.");
        }

        var userEntity = userEntityOp.get();

        var validatorDto = UpdateUserUseCaseValidatorDto.builder()
                .searchedUserId(userToBeUpdatedId)
                .searchedUserCompanyId(userEntity.getCompany().getId())
                .loggedUserId(idLoggedUser)
                .loggedUserRole(loggedUserRole)
                .loggedUserCompanyId(loggedUserCompanyId)
                .build();

        if (!validatorService.execute(validatorDto)) {
            throw new InsufficientCredentialsException();
        }

        var hasChanges = updateUserFields(userEntity, input);

        if (hasChanges) {
            userEntity.setUpdatedBy(idLoggedUser);
            userRepository.update(userEntity);

            var userDomain = domainMapper.toDomain(userEntity);
            var userOutputDto = adapterMapper.toDto(userDomain);

            cacheGateway.put(UserCacheKeys.USER.getKey().formatted(userToBeUpdatedId), userOutputDto);
            cacheGateway.deleteAllFrom(UserCacheKeys.USERS_PATTERN.getKey());
        }
    }

    private boolean updateUserFields(UserEntity userEntity, UpdateUserInputDto updateDto) {
        boolean hasChanges = false;

        if (comparatorService.hasChanges(updateDto.getName(), userEntity.getName())) {
            userEntity.setName(updateDto.getName());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getLastName(), userEntity.getLastName())) {
            userEntity.setLastName(updateDto.getLastName());
            hasChanges = true;
        }

        var newPhone = updateDto.getPhone() == null ? null : Long.parseLong(updateDto.getPhone());
        if (comparatorService.hasChanges(newPhone, userEntity.getPhone())) {
            var userExists = userRepository.findByPhone(newPhone);

            if (userExists.isPresent()) {
                throw new UserAlreadyExistsException("Já existe um usuário com esse telefone.");
            }

            userEntity.setPhone(newPhone);
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getEmail(), userEntity.getEmail())) {
            var userExists = userRepository.findByEmail(updateDto.getEmail());

            if (userExists.isPresent()) {
                throw new UserAlreadyExistsException("Já existe um usuário com esse email.");
            }

            userEntity.setEmail(updateDto.getEmail());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getPassword(), userEntity.getPassword())) {
            userEntity.setPassword(updateDto.getPassword());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getCpf(), userEntity.getCpf())) {
            var userExists = userRepository.findByCpf(updateDto.getCpf());

            if (userExists.isPresent()) {
                throw new UserAlreadyExistsException("Já existe um usuário com esse CPF.");
            }

            userEntity.setCpf(updateDto.getCpf());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getBirthDate(), userEntity.getBirthDate())) {
            userEntity.setBirthDate(updateDto.getBirthDate());
            hasChanges = true;
        }

        var newSex = updateDto.getSex() == null ? null : updateDto.getSex().charAt(0);
        if (comparatorService.hasChanges(newSex, userEntity.getSex())) {
            userEntity.setSex(newSex);
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getStreet(), userEntity.getStreet())) {
            userEntity.setStreet(updateDto.getStreet());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getStreetNumber(), userEntity.getStreetNumber())) {
            userEntity.setStreetNumber(updateDto.getStreetNumber());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getDistrict(), userEntity.getDistrict())) {
            userEntity.setDistrict(updateDto.getDistrict());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getComplement(), userEntity.getComplement())) {
            userEntity.setComplement(updateDto.getComplement());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getCep(), userEntity.getCep())) {
            userEntity.setCep(updateDto.getCep());
            hasChanges = true;
        }

        if (comparatorService.hasChanges(updateDto.getIdCity(), userEntity.getCity().getId())) {
            userEntity.setCity(CityEntity.builder().id(updateDto.getIdCity()).build());
            hasChanges = true;
        }

        return hasChanges;
    }
}
