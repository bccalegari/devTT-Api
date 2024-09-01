package br.com.devtt.infrastructure.adapters.gateway.database.repositories;

import br.com.devtt.core.abstractions.infrastructure.adapters.gateway.database.repositories.CompanyRepository;
import br.com.devtt.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("HibernateCompanyRepository")
public class HibernateCompanyRepository implements CompanyRepository<CompanyEntity> {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public Optional<CompanyEntity> findByCnpj(String cnpj) {
        return entityManager.createQuery("SELECT c FROM CompanyEntity c WHERE c.cnpj = :cnpj", CompanyEntity.class)
                .setParameter("cnpj", cnpj)
                .getResultStream()
                .findFirst();
    }

    @Override
    public CompanyEntity save(CompanyEntity entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }
}