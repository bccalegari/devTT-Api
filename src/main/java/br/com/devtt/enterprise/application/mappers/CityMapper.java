package br.com.devtt.enterprise.application.mappers;

import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.domain.entities.City;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.CityEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CityMapper extends DomainMapper<City, CityEntity> {}