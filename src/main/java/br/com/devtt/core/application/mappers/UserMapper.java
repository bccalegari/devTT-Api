package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.valueobjects.Sex;
import br.com.devtt.core.domain.entities.User;
import br.com.devtt.core.domain.valueobjects.Address;
import br.com.devtt.core.domain.valueobjects.Auditing;
import br.com.devtt.core.domain.valueobjects.Cep;
import br.com.devtt.core.domain.valueobjects.Cpf;
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

    @Mapping(target = "cpf", source = "cpf.value")
    @Mapping(target = "sex", source = "sex.code")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "streetNumber", source = "address.streetNumber")
    @Mapping(target = "district", source = "address.district")
    @Mapping(target = "complement", source = "address.complement")
    @Mapping(target = "cep", source = "address.cep.value")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "createdBy", source = "auditing.createdBy")
    UserEntity toEntity(User user);

    default Sex mapSex(Character sex) {
        return Sex.fromCode(sex);
    }

    @Named("mapAddress")
    Address mapAddress(UserEntity userEntity);

    default Cep mapCep(String cep) {
        return new Cep(cep);
    }

    default Cpf mapCpf(String cpf) {
        return new Cpf(cpf);
    }

    @Named("mapAuditing")
    Auditing mapAuditing(UserEntity userEntity);
}