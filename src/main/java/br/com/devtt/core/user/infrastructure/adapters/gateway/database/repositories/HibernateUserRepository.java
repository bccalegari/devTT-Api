package br.com.devtt.core.user.infrastructure.adapters.gateway.database.repositories;


import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway.Page;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PageImpl;
import br.com.devtt.enterprise.infrastructure.adapters.gateway.database.PaginationParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("HibernateUserRepository")
public class HibernateUserRepository implements UserRepository<UserEntity> {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return entityManager.createQuery("""
                        SELECT
                            u
                        FROM
                            UserEntity u
                        WHERE
                            u.id = :id
                            AND u.deletedDt IS NULL
                        """, UserEntity.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findByPhone(Long phone) {
        return entityManager.createQuery("""
                        SELECT
                            u
                        FROM
                            UserEntity u
                        WHERE
                            u.phone = :phone
                            AND u.deletedDt IS NULL
                        """, UserEntity.class)
                .setParameter("phone", phone)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return entityManager.createQuery("""
                        SELECT
                            u
                        FROM
                            UserEntity u
                        WHERE
                            u.email = :email
                            AND u.deletedDt IS NULL
                        """, UserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findByCpf(String cpf) {
        return entityManager.createQuery("""
                        SELECT
                            u
                        FROM
                            UserEntity u
                        WHERE
                            u.cpf = :cpf
                            AND u.deletedDt IS NULL
                        """, UserEntity.class)
                .setParameter("cpf", cpf)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<UserEntity> findByPhoneOrEmailOrCpf(Long phone, String email, String cpf) {
        return entityManager.createQuery("""
                        SELECT
                            u
                        FROM
                            UserEntity u
                        WHERE
                            (u.phone = :phone OR u.email = :email OR u.cpf = :cpf)
                            AND u.deletedDt IS NULL
                        """, UserEntity.class)
                .setParameter("phone", phone)
                .setParameter("email", email)
                .setParameter("cpf", cpf)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Page<UserEntity> findAll(PaginationParams paginationParams, String search, Integer idCompany) {
        var companyIdQuery = idCompany != null && idCompany > 0
                ? "AND c.company.id = :idCompany "
                : "";

        var searchQuery = search != null && !search.isBlank()
                ? """
                AND (
                    LOWER(c.name || ' ' || c.lastName) LIKE ('%' || LOWER(:search) || '%')
                    OR LOWER(c.email) LIKE ('%' || LOWER(:search) || '%')
                    OR CAST(c.phone AS STRING) = :search
                    OR CAST(c.cpf AS STRING) = :search
                )
                """
                : "";

        var usersQuery = entityManager.createQuery(
                        """
                        SELECT
                            c
                        FROM
                            UserEntity c
                        WHERE
                            c.deletedDt IS NULL
                        """ + companyIdQuery + searchQuery,
                        UserEntity.class)
                .setFirstResult(paginationParams.getPage())
                .setMaxResults(paginationParams.getSize());

        if (idCompany != null && idCompany > 0) {
            usersQuery.setParameter("idCompany", idCompany);
        }

        if (search != null && !search.isBlank()) {
            usersQuery.setParameter("search", search);
        }

        var users = usersQuery.getResultList();

        var totalElementsQuery = entityManager.createQuery("""
                SELECT
                    COUNT(c)
                FROM
                    UserEntity c
                WHERE
                    c.deletedDt IS NULL
                """ + companyIdQuery + searchQuery, Long.class);

        if (idCompany != null && idCompany > 0) {
            totalElementsQuery.setParameter("idCompany", idCompany);
        }

        if (search != null && !search.isBlank()) {
            totalElementsQuery.setParameter("search", search);
        }

        var totalElements = totalElementsQuery.getSingleResult();

        var totalPages = (long) Math.ceil((double) totalElements / paginationParams.getSize());

        return new PageImpl<>(
                paginationParams.getPage(), paginationParams.getSize(), paginationParams.getCurrentPage(),
                totalElements, totalPages, users
        );
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        String insertQuery = """
                INSERT INTO
                    client.user (name, "lastName", phone, email, password, cpf, "birthDate", sex,
                    street, "streetNumber", district, complement, cep, "idCity", "createdBy", "idRole", "idCompany")
                VALUES
                    (:name, :lastName, :phone, :email, :password, :cpf, :birthDate, :sex, :street, :streetNumber,
                    :district, :complement, :cep, :idCity, :createdBy, :idRole, :idCompany)
                RETURNING id;
                """;

        var idNewUser = Long.parseLong(entityManager.createNativeQuery(insertQuery)
                .setParameter("name", userEntity.getName())
                .setParameter("lastName", userEntity.getLastName())
                .setParameter("phone", userEntity.getPhone())
                .setParameter("email", userEntity.getEmail())
                .setParameter("password", userEntity.getPassword())
                .setParameter("cpf", userEntity.getCpf())
                .setParameter("birthDate", userEntity.getBirthDate())
                .setParameter("sex", userEntity.getSex())
                .setParameter("street", userEntity.getStreet())
                .setParameter("streetNumber", userEntity.getStreetNumber())
                .setParameter("district", userEntity.getDistrict())
                .setParameter("complement", userEntity.getComplement())
                .setParameter("cep", userEntity.getCep())
                .setParameter("idCity", userEntity.getCity().getId())
                .setParameter("createdBy", userEntity.getCreatedBy())
                .setParameter("idRole", userEntity.getRole().getId())
                .setParameter("idCompany", userEntity.getCompany().getId())
                .getSingleResult().toString());

        entityManager.flush();
        return entityManager.find(UserEntity.class, idNewUser);
    }

    @Override
    @Modifying
    public void update(UserEntity entity) {
        var query = """
                UPDATE
                    UserEntity u
                SET
                    u.name = :name,
                    u.lastName = :lastName,
                    u.phone = :phone,
                    u.email = :email,
                    u.password = :password,
                    u.cpf = :cpf,
                    u.birthDate = :birthDate,
                    u.sex = :sex,
                    u.street = :street,
                    u.streetNumber = :streetNumber,
                    u.district = :district,
                    u.complement = :complement,
                    u.cep = :cep,
                    u.city = :city,
                    u.updatedDt = CURRENT_TIMESTAMP,
                    u.updatedBy = :updatedBy
                WHERE
                    u.id = :id
                    AND u.deletedDt IS NULL
                """;
        entityManager.createQuery(query)
                .setParameter("name", entity.getName())
                .setParameter("lastName", entity.getLastName())
                .setParameter("phone", entity.getPhone())
                .setParameter("email", entity.getEmail())
                .setParameter("password", entity.getPassword())
                .setParameter("cpf", entity.getCpf())
                .setParameter("birthDate", entity.getBirthDate())
                .setParameter("sex", entity.getSex())
                .setParameter("street", entity.getStreet())
                .setParameter("streetNumber", entity.getStreetNumber())
                .setParameter("district", entity.getDistrict())
                .setParameter("complement", entity.getComplement())
                .setParameter("cep", entity.getCep())
                .setParameter("city", entity.getCity())
                .setParameter("updatedBy", entity.getUpdatedBy())
                .setParameter("id", entity.getId())
                .executeUpdate();
    }

    @Override
    @Modifying
    public void delete(UserEntity entity) {
        entityManager.createQuery("""
                UPDATE
                    UserEntity u
                SET
                    u.updatedDt = CURRENT_TIMESTAMP,
                    u.updatedBy = :updatedBy,
                    u.deletedDt = CURRENT_TIMESTAMP,
                    u.deletedBy = :deletedBy
                WHERE
                    u.id = :id
                """)
                .setParameter("id", entity.getId())
                .setParameter("updatedBy", entity.getUpdatedBy())
                .setParameter("deletedBy", entity.getDeletedBy())
                .executeUpdate();
    }

    @Override
    @Modifying
    public void deleteByCompanyId(Integer idCompany, Long idLoggedUser) {
        entityManager.createQuery("""
                UPDATE
                    UserEntity u
                SET
                    u.updatedDt = CURRENT_TIMESTAMP,
                    u.updatedBy = :updatedBy,
                    u.deletedDt = CURRENT_TIMESTAMP,
                    u.deletedBy = :deletedBy
                WHERE
                    u.deletedDt IS NULL
                    AND u.company.id = :idCompany
                """)
                .setParameter("idCompany", idCompany)
                .setParameter("updatedBy", idLoggedUser)
                .setParameter("deletedBy", idLoggedUser)
                .executeUpdate();
    }

}