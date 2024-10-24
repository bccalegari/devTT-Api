package br.com.devtt.core.user.infrastructure.adapters.mappers;

import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.user.domain.entities.User;
import br.com.devtt.core.user.domain.valueobjects.Cpf;
import br.com.devtt.core.user.domain.valueobjects.Sex;
import br.com.devtt.core.user.infrastructure.adapters.dto.responses.GetUserOutputDto;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.enterprise.domain.entities.City;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.enterprise.domain.valueobjects.Cep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GetUserOutputDtoMapper extends AdapterMapper<User, GetUserOutputDto> {
    default String mapCpf(Cpf cpf) {
        return cpf.getValue();
    }
    default Cpf mapCpf(String cpf) {
        return new Cpf(cpf);
    }
    default String mapSex(Sex sex) {
        return sex.getCode().toString();
    }
    default String mapCep(Cep cep) {
        return cep.getValue();
    }
    default Cep mapCep(String cep) {
        return new Cep(cep);
    }
    default String mapCity(City city) {
        return city.getName();
    }
    default City mapCity(String city) {
        return City.builder().name(city).build();
    }
    default Company mapCompany(String company) {
        return Company.builder().name(company).build();
    }
    default Cnpj mapCnpj(String cnpj) {
        return new Cnpj(cnpj);
    }
    default String mapCnpj(Cnpj cnpj) {
        return cnpj.getValue();
    }
    default String mapCompany(Company company) {
        return company.getName();
    }
    default Role mapRole(String role) {
        return Role.builder().name(role).build();
    }
    default String mapRole(Role role) {
        return role.getName();
    }

    @Mapping(target = "address.street", source = "address.street")
    @Mapping(target = "address.streetNumber", source = "address.streetNumber")
    @Mapping(target = "address.district", source = "address.district")
    @Mapping(target = "address.complement", source = "address.complement")
    @Mapping(target = "address.cep", source = "address.cep")
    @Mapping(target = "address.city.id", source = "address.city.id")
    @Mapping(target = "address.city.name", source = "address.city.name")
    @Mapping(target = "address.city.state.id", source = "address.city.state.id")
    @Mapping(target = "address.city.state.name", source = "address.city.state.name")
    @Mapping(target = "company.id", source = "company.id")
    @Mapping(target = "company.name", source = "company.name")
    @Mapping(target = "company.cnpj", source = "company.cnpj")
    @Mapping(target = "role.id", source = "role.id")
    @Mapping(target = "role.name", source = "role.name")
    GetUserOutputDto toDto(User user);
}