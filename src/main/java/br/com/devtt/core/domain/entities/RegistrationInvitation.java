package br.com.devtt.core.domain.entities;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class RegistrationInvitation {
    private Long id;
    private String email;
    private Long idUser;
    private Long createdBy;
    private LocalDateTime createdDt;
    private Long updatedBy;
    private LocalDateTime updatedDt;
    private Long deletedBy;
    private LocalDateTime deletedDt;
    private LocalDateTime expirationDt;
    private String token;
    private LocalDateTime consumedDt;

    public static RegistrationInvitation create(Long idUser, String email, Long createdBy) {
        return RegistrationInvitation.builder()
                .idUser(idUser)
                .email(email)
                .createdBy(createdBy)
                .expirationDt(LocalDateTime.now().plusDays(7))
                .token(generateToken(idUser, email, createdBy))
                .build();
    }

    private static String generateToken(Long idUser, String email, Long createdBy) {
        var randomSalt = Double.doubleToLongBits(Math.random());
        var combinedValue = (idUser.hashCode() ^ email.hashCode() ^ createdBy.hashCode() ^ randomSalt) << 1;
        combinedValue = ~combinedValue;
        combinedValue = (combinedValue << 1) | (combinedValue >>> 1);
        var secondRandomSalt = Double.doubleToLongBits(Math.random());
        combinedValue ^= secondRandomSalt;
        return Long.toHexString(combinedValue);
    }

    public void consume(Long idUser) {
        updatedBy = idUser;
        var now = LocalDateTime.now();
        consumedDt = now;
        updatedDt = now;
    }
}