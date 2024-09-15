package br.com.devtt.enterprise.application.mappers;

import br.com.devtt.enterprise.abstractions.application.mappers.DomainMapper;
import br.com.devtt.enterprise.domain.entities.Role;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.entities.RoleEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoleMapper extends DomainMapper<Role, RoleEntity> {}