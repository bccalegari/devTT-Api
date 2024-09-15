package br.com.devtt.core.user.invitation.application.mappers;

import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.user.invitation.domain.entities.RegistrationInvitation;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserRegistrationInvitationMapper extends DomainMapper<RegistrationInvitation, UserRegistrationInvitationEntity> {
    @Mapping(target = "user", source = "registrationInvitation", qualifiedByName = "mapUserEntity")
    UserRegistrationInvitationEntity toEntity(RegistrationInvitation registrationInvitation);

    @Named("mapUserEntity")
    default UserEntity mapUserEntity(RegistrationInvitation registrationInvitation) {
        return UserEntity.builder()
                .id(registrationInvitation.getIdUser())
                .build();
    }
}