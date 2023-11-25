package de.eric.bankingapp.user.model;

import lombok.NonNull;

public record EditRequest(
        String email,
        String firstName,
        String lastName,
        String role,
        boolean emailVerified) {
}
