package br.com.devtt.core.user.infrastructure.adapters.mappers;

import br.com.devtt.core.user.domain.valueobjects.Cpf;
import br.com.devtt.core.user.domain.valueobjects.Sex;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.domain.entities.City;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.infrastructure.adapters.dto.requests.CreateUserInputDto;
import br.com.devtt.enterprise.domain.valueobjects.Address;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.enterprise.domain.valueobjects.Cep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CreateUserInputDtoMapper extends AdapterMapper<User, CreateUserInputDto> {
    default Sex mapSex(String sex) {
        return Sex.fromCode(sex);
    }

    @Mapping(target = "address", source = "dto", qualifiedByName = "mapAddress")
    @Mapping(target = "auditing", source = "dto", qualifiedByName = "mapAuditing")
    @Mapping(target = "company", source = "dto", qualifiedByName = "mapCompany")
    @Mapping(target = "role", source = "dto", qualifiedByName = "mapRole")
    User toDomain(CreateUserInputDto dto);

    @Named("mapAddress")
    @Mapping(target = "city", source = "idCity", qualifiedByName = "mapCity")
    Address mapAddress(CreateUserInputDto dto);

    @Named("mapCity")
    default City mapCity(Long idCity) {
        return City.builder().id(idCity).build();
    }

    default Cep mapCep(String cep) {
        return new Cep(cep);
    }

    default Cpf mapCpf(String cpf) {
        return new Cpf(cpf);
    }

    default String mapCpf(Cpf cpf) {
        return cpf.getValue();
    }

    @Named("mapAuditing")
    Auditing mapAuditing(CreateUserInputDto dto);

    @Named("mapCompany")
    default Company mapCompany(CreateUserInputDto dto) {
        return Company.builder().id(dto.getIdCompany()).build();
    }

    @Named("mapRole")
    default Role mapRole(CreateUserInputDto dto) {
        return Role.builder().id(dto.getIdRole()).build();
    }
}