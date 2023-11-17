package de.eric.bankingapp.registration.model;

public record RegistrationRequest (
        String email,
        String firstName,
        String lastName,
        String password,
        String verificationRedirect
) {
}
