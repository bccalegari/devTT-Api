package br.com.devtt.core.user.invitation.infrastructure.adapters.gateway.database.entities;

import br.com.devtt.core.user.infrastructure.adapters.gateway.database.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"userRegistrationInvitation\"", schema = "client")
@EqualsAndHashCode(of = "id")
public class UserRegistrationInvitationEntity {
    @Id
    private Long id;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"idUser\"")
    private UserEntity user;

    @Column(name = "\"createdBy\"")
    private Long createdBy;

    @Column(name = "\"createdDt\"")
    private LocalDateTime createdDt;

    @Column(name = "\"updatedBy\"")
    private Long updatedBy;

    @Column(name = "\"updatedDt\"")
    private LocalDateTime updatedDt;

    @Column(name = "\"deletedBy\"")
    private Long deletedBy;

    @Column(name = "\"deletedDt\"")
    private LocalDateTime deletedDt;

    @Column(name = "\"expirationDt\"")
    private LocalDateTime expirationDt;

    private String token;

    @Column(name = "\"consumedDt\"")
    private LocalDateTime consumedDt;
}