package br.com.devtt.core.user.infrastructure.adapters.gateway.database.repositories;


import br.com.devtt.core.user.abstractions.infrastructure.adapters.gateway.UserRepository;
import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public void delete(UserEntity entity) {
        entityManager.createQuery("""
                UPDATE
                    UserEntity u
                SET
                    u.deletedBy = :deletedBy,
                    u.deletedDt = current_timestamp
                WHERE
                    u.id = :id
                """)
                .setParameter("id", entity.getId())
                .setParameter("deletedBy", entity.getDeletedBy())
                .executeUpdate();
    }
}