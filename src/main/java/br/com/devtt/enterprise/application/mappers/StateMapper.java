package br.com.devtt.enterprise.application.mappers;

import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.domain.entities.State;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.StateEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface StateMapper extends DomainMapper<State, StateEntity> {}