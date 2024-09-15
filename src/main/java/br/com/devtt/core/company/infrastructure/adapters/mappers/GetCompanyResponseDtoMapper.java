package br.com.devtt.core.company.infrastructure.adapters.mappers;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.mappers.AdapterMapper;
import br.com.devtt.core.company.domain.entities.Company;
import br.com.devtt.core.company.domain.valueobjects.Cnpj;
import br.com.devtt.core.company.infrastructure.adapters.dto.responses.GetCompanyOutputDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GetCompanyResponseDtoMapper extends AdapterMapper<Company, GetCompanyOutputDto> {
    default String mapCnpj(Cnpj cnpj) {
        return cnpj.getValue();
    }
    default Cnpj mapCnpj(String cnpj) {
        return new Cnpj(cnpj);
    }
}