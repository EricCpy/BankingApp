package de.eric.bankingapp.user.model.request;

import lombok.Builder;

@Builder
public record CreationRequest(
        String email,
        String firstName,
        String lastName,
        String password,
        String role,
        boolean emailVerified) {
}
