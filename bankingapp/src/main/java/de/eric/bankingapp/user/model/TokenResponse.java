package de.eric.bankingapp.user.model;

public record TokenResponse(
        String email,
        String token
) {
}
