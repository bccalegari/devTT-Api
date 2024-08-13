package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.Sex;
import br.com.devtt.core.domain.entities.User;
import br.com.devtt.core.domain.valueobjects.Address;
import br.com.devtt.core.domain.valueobjects.Auditing;
import br.com.devtt.core.domain.valueobjects.Cep;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends DomainMapper<User, UserEntity> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "address", source = "userEntity", qualifiedByName = "mapAddress")
    @Mapping(target = "auditing", source = "userEntity", qualifiedByName = "mapAuditing")
    User toDomain(UserEntity userEntity);
    UserEntity toEntity(User user);

    default Sex map(Character sex) {
        return Sex.fromCode(sex);
    }
    default Character map(Sex sex) {
        return sex.getCode();
    }

    @Named("mapAddress")
    Address mapAddress(UserEntity userEntity);

    default Cep mapCep(String cep) {
        return new Cep(cep);
    }

    @Named("mapAuditing")
    Auditing mapAuditing(UserEntity userEntity);
}