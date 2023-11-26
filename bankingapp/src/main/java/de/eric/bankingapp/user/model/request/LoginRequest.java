package de.eric.bankingapp.user.model.request;

public record LoginRequest(
        String email,
        String password
) {

}
