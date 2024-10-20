package br.com.devtt.core.company.application.mappers;

import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.enterprise.domain.valueobjects.Auditing;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CompanyMapper extends DomainMapper<Company, CompanyEntity> {
    @Mapping(target = "auditing", source = "companyEntity", qualifiedByName = "mapAuditing")
    Company toDomain(CompanyEntity companyEntity);

    @Mapping(target = "createdBy", source = "auditing.createdBy")
    CompanyEntity toEntity(Company company);

    default String map(Cnpj cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.getValue();
    }

    default Cnpj map(String cnpj) {
        return new Cnpj(cnpj);
    }

    @Named("mapAuditing")
    Auditing mapAuditing(CompanyEntity userEntity);
}