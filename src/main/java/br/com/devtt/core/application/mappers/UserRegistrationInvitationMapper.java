package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.RegistrationInvitation;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserRegistrationInvitationMapper extends DomainMapper<RegistrationInvitation, UserRegistrationInvitationEntity> {
    UserRegistrationInvitationMapper INSTANCE = Mappers.getMapper(UserRegistrationInvitationMapper.class);

    @Mapping(target = "user", source = "registrationInvitation", qualifiedByName = "mapUserEntity")
    UserRegistrationInvitationEntity toEntity(RegistrationInvitation registrationInvitation);

    @Named("mapUserEntity")
    default UserEntity mapUserEntity(RegistrationInvitation registrationInvitation) {
        return UserEntity.builder()
                .id(registrationInvitation.getIdUser())
                .build();
    }
}