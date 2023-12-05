package de.eric.bankingapp.registration.model;

import lombok.NonNull;

public record EmailRequest(
        @NonNull
        String email,
        String verificationRedirect
) {

}
