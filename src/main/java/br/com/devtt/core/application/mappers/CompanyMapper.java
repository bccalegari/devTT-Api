package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.Company;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CompanyMapper extends DomainMapper<Company, CompanyEntity> {}