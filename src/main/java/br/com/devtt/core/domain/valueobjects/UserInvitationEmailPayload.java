package br.com.devtt.core.domain.valueobjects;

import lombok.Builder;

@Builder
public record UserInvitationEmailPayload(
        String fullName,
        String email,
        String token,
        String creatorName
) {}