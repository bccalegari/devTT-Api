package br.com.devtt.core.user.invitation.infrastructure.adapters.dto;

import lombok.Builder;

@Builder
public record UserInvitationEmailPayload(
        String fullName,
        String email,
        String token,
        String creatorName
) {}