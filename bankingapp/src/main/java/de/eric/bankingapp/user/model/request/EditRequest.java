package de.eric.bankingapp.user.model.request;

public record EditRequest(
        String email,
        String firstName,
        String lastName,
        String role,
        boolean emailVerified) {
}
