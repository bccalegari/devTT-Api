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
    public Optional<CompanyEntity> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(CompanyEntity.class, id));
    }

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

    @Override
    public void update(CompanyEntity entity) {
        String query = """
                UPDATE CompanyEntity c
                SET c.name = :name, c.cnpj = :cnpj, c.updatedDt = CURRENT_TIMESTAMP, c.updatedBy = :idLoggedUser
                WHERE c.id = :id
                """;
        entityManager.createQuery(query)
                .setParameter("name", entity.getName())
                .setParameter("cnpj", entity.getCnpj())
                .setParameter("idLoggedUser", entity.getUpdatedBy())
                .setParameter("id", entity.getId())
                .executeUpdate();
    }
}