package de.eric.bankingapp.user.model.response;

public record TokenResponse(
        String email,
        String token
) {
}
