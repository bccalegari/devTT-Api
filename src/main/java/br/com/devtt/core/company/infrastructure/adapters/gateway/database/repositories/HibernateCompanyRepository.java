package br.com.devtt.core.company.infrastructure.adapters.gateway.database.repositories;

import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.core.company.abstractions.infrastructure.adapters.gateway.CompanyRepository;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PageImpl;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import br.com.devtt.core.company.infrastructure.adapters.gateway.database.entities.CompanyEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("HibernateCompanyRepository")
public class HibernateCompanyRepository implements CompanyRepository<CompanyEntity> {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public Optional<CompanyEntity> findById(Integer id) {
        return entityManager.createQuery("SELECT c FROM CompanyEntity c WHERE c.id = :id AND c.deletedDt IS NULL",
                        CompanyEntity.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<CompanyEntity> findByCnpj(String cnpj) {
        return entityManager.createQuery("SELECT c FROM CompanyEntity c WHERE c.cnpj = :cnpj AND c.deletedDt IS NULL",
                        CompanyEntity.class)
                .setParameter("cnpj", cnpj)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Page<CompanyEntity> findAll(PaginationParams paginationParams) {
        var companies = entityManager.createQuery("SELECT c FROM CompanyEntity c WHERE c.deletedDt IS NULL",
                        CompanyEntity.class)
                .setFirstResult(paginationParams.getPage())
                .setMaxResults(paginationParams.getSize())
                .getResultList();

        var totalElements = entityManager.createQuery("SELECT COUNT(c) FROM CompanyEntity c WHERE c.deletedDt IS NULL",
                        Long.class)
                .getSingleResult();

        var totalPages = (long) Math.ceil((double) totalElements / paginationParams.getSize());

        return new PageImpl<>(
                paginationParams.getPage(), paginationParams.getSize(), paginationParams.getCurrentPage(),
                totalElements, totalPages, companies
        );
    }

    @Override
    public Page<CompanyEntity> findAllByNameOrCnpj(String name, String cnpj, PaginationParams paginationParams) {
        var query = """
                SELECT c
                FROM CompanyEntity c
                WHERE (c.name LIKE :name OR c.cnpj LIKE :cnpj)
                AND c.deletedDt IS NULL
                """;

        var companies = entityManager.createQuery(query, CompanyEntity.class)
                .setFirstResult(paginationParams.getPage())
                .setMaxResults(paginationParams.getSize())
                .setParameter("name", "%" + name + "%")
                .setParameter("cnpj", "%" + cnpj + "%")
                .getResultList();

        var totalElements = entityManager.createQuery("SELECT COUNT(c) FROM CompanyEntity c WHERE c.deletedDt IS NULL",
                        Long.class)
                .getSingleResult();

        var totalPages = (long) Math.ceil((double) totalElements / paginationParams.getSize());

        return new PageImpl<>(
                paginationParams.getPage(), paginationParams.getSize(), paginationParams.getCurrentPage(),
                totalElements, totalPages, companies
        );
    }

    @Override
    public List<String> getAllNames() {
        return entityManager.createQuery("SELECT c.name FROM CompanyEntity c WHERE c.deletedDt IS NULL", String.class)
                .getResultList();
    }

    @Override
    public List<String> getAllCnpjs() {
        return entityManager.createQuery("SELECT c.cnpj FROM CompanyEntity c WHERE c.deletedDt IS NULL", String.class)
                .getResultList();
    }

    @Override
    public CompanyEntity save(CompanyEntity entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public void update(CompanyEntity entity) {
        var query = """
                UPDATE CompanyEntity c
                SET c.name = :name, c.cnpj = :cnpj, c.updatedDt = CURRENT_TIMESTAMP, c.updatedBy = :idLoggedUser
                WHERE c.id = :id
                AND c.deletedDt IS NULL
                """;
        entityManager.createQuery(query)
                .setParameter("name", entity.getName())
                .setParameter("cnpj", entity.getCnpj())
                .setParameter("idLoggedUser", entity.getUpdatedBy())
                .setParameter("id", entity.getId())
                .executeUpdate();
    }

    @Override
    public void delete(CompanyEntity entity) {
        var query = """
                UPDATE CompanyEntity c
                SET c.updatedDt = CURRENT_TIMESTAMP, c.updatedBy = :idLoggedUser, 
                c.deletedDt = CURRENT_TIMESTAMP, c.deletedBy = :idLoggedUser
                WHERE c.id = :id
                AND c.deletedDt IS NULL
                """;
        entityManager.createQuery(query)
                .setParameter("idLoggedUser", entity.getDeletedBy())
                .setParameter("id", entity.getId())
                .executeUpdate();
    }
}