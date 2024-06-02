package br.com.devtt.infrastructure.adapters.gateway.database.repositories;

import br.com.devtt.core.abstractions.adapters.gateway.database.repositories.UserRepository;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("HibernateUserRepository")
public interface HibernateUserRepository extends JpaRepository<UserEntity, Long>, UserRepository {}