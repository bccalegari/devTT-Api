package br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.repositories;

import br.com.devtt.core.user.invitation.abstractions.infrastructure.adapters.gateway.UserRegistrationInvitationRepository;
import br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities.UserRegistrationInvitationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier("HibernateUserRegistrationInvitationRepository")
public class HibernateUserRegistrationInvitationRepository
        implements UserRegistrationInvitationRepository<UserRegistrationInvitationEntity> {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public Optional<UserRegistrationInvitationEntity> findByUserId(Long userId) {
        return entityManager.createQuery("""
                SELECT
                    eri
                FROM
                    UserRegistrationInvitationEntity eri
                WHERE
                    eri.user.id = :userId
                    AND eri.deletedDt IS NULL
                """, UserRegistrationInvitationEntity.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public void disableRegistrationInvitation(Long idUserRegistrationInvitation, Long idLoggedUser) {
        entityManager.createQuery("""
                UPDATE
                    UserRegistrationInvitationEntity eri
                SET
                    eri.updatedDt = CURRENT_TIMESTAMP,
                    eri.updatedBy = :idLoggedUser,
                    eri.deletedDt = CURRENT_TIMESTAMP,
                    eri.deletedBy = :idLoggedUser
                WHERE
                    eri.id = :idUserRegistrationInvitation
                """)
                .setParameter("idUserRegistrationInvitation", idUserRegistrationInvitation)
                .setParameter("idLoggedUser", idLoggedUser)
                .executeUpdate();

    }

    @Override
    public void disableAllRegistrationInvitationsByUserId(Long userId, Long idLoggedUser) {
        entityManager.createQuery("""
                UPDATE
                    UserRegistrationInvitationEntity eri
                SET
                    eri.updatedDt = CURRENT_TIMESTAMP,
                    eri.updatedBy = :idLoggedUser,
                    eri.deletedDt = CURRENT_TIMESTAMP,
                    eri.deletedBy = :idLoggedUser
                WHERE
                    eri.user.id = :userId
                """)
                .setParameter("userId", userId)
                .setParameter("idLoggedUser", idLoggedUser)
                .executeUpdate();
    }

    @Override
    public void disableAllRegistrationInvitationsByCompanyId(Integer companyId, Long idLoggedUser) {
        entityManager.createQuery("""
                UPDATE
                    UserRegistrationInvitationEntity eri
                SET
                    eri.updatedDt = CURRENT_TIMESTAMP,
                    eri.updatedBy = :idLoggedUser,
                    eri.deletedDt = CURRENT_TIMESTAMP,
                    eri.deletedBy = :idLoggedUser
                WHERE
                    eri.user.company.id = :companyId
                """)
                .setParameter("companyId", companyId)
                .setParameter("idLoggedUser", idLoggedUser)
                .executeUpdate();
    }

    @Override
    public UserRegistrationInvitationEntity save(UserRegistrationInvitationEntity entity) {
        var insertQuery = """
                INSERT INTO
                    client."userRegistrationInvitation" ("idUser", email, "createdBy", "expirationDt", token)
                VALUES
                    (:idUser, :email, :createdBy, :expirationDt, :token)
                RETURNING id;
                """;
        var idNewUserRegistrationInvitation = Long.parseLong(entityManager.createNativeQuery(insertQuery)
                .setParameter("idUser", entity.getUser().getId())
                .setParameter("email", entity.getEmail())
                .setParameter("createdBy", entity.getCreatedBy())
                .setParameter("expirationDt", entity.getExpirationDt())
                .setParameter("token", entity.getToken())
                .getSingleResult().toString());

        entityManager.flush();
        return entityManager.find(UserRegistrationInvitationEntity.class, idNewUserRegistrationInvitation);
    }
}