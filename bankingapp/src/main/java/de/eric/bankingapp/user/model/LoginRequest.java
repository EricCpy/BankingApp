package de.eric.bankingapp.user.model;

public record LoginRequest(
        String email,
        String password
) {

}
