package br.com.devtt.core.application.mappers;

import br.com.devtt.core.abstractions.mappers.DomainMapper;
import br.com.devtt.core.domain.entities.State;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.StateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface StateMapper extends DomainMapper<State, StateEntity> {
    StateMapper INSTANCE = Mappers.getMapper(StateMapper.class);
}