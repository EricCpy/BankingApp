package de.eric.bankingapp.registration.model;

import lombok.NonNull;

public record RegistrationRequest (
        @NonNull
        String email,
        @NonNull
        String firstName,
        @NonNull
        String lastName,
        @NonNull
        String password,
        String verificationRedirect
) {
}
