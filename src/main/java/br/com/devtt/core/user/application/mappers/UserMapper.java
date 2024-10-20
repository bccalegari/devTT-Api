package br.com.devtt.core.user.application.mappers;

import br.com.devtt.core.user.domain.valueobjects.Cpf;
import br.com.devtt.core.user.domain.valueobjects.Sex;
import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.enterprise.domain.valueobjects.Address;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.enterprise.domain.valueobjects.Cep;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper extends DomainMapper<User, UserEntity> {
    @Mapping(target = "address", source = "userEntity", qualifiedByName = "mapAddress")
    @Mapping(target = "auditing", source = "userEntity", qualifiedByName = "mapAuditing")
    @Mapping(target = "company", source = "userEntity", qualifiedByName = "mapCompany")
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

    @Named("mapCompany")
    @Mapping(target = "cnpj", source = "company.cnpj")
    Company mapCompany(UserEntity userEntity);

    default Cep mapCep(String cep) {
        return new Cep(cep);
    }

    default Cpf mapCpf(String cpf) {
        return new Cpf(cpf);
    }

    default String mapCnpj(Cnpj cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.getValue();
    }

    default Cnpj mapCnpj(String cnpj) {
        return new Cnpj(cnpj);
    }

    @Named("mapAuditing")
    Auditing mapAuditing(UserEntity userEntity);
}