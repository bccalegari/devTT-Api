package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.City;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CityMapper extends DomainMapper<City, CityEntity> {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);
}