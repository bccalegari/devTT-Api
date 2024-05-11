package br.com.devTT.core.application.mappers;

import br.com.devTT.core.abstractions.mappers.DomainMapper;
import br.com.devTT.core.domain.entities.User;
import br.com.devTT.infrastructure.adapters.gateway.database.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserDomainMapper extends DomainMapper<User, UserEntity> {
    UserDomainMapper INSTANCE = Mappers.getMapper(UserDomainMapper.class);
    User toDomain(UserEntity userEntity);
}
