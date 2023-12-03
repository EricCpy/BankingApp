package de.eric.bankingapp.user.model.request;

public record CreationRequest(
        String email,
        String firstName,
        String lastName,
        String password,
        String role,
        boolean emailVerified) {
}
