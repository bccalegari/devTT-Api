package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.Company;
import br.com.devtt.core.domain.valueobjects.Cnpj;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CompanyMapper extends DomainMapper<Company, CompanyEntity> {
    default String map(Cnpj cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.getValue();
    }

    default Cnpj map(String cnpj) {
        return new Cnpj(cnpj);
    }
}