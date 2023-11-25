package de.eric.bankingapp.user.model;

import lombok.NonNull;

public record BlockRequest(
        @NonNull
        String email,
        boolean block
) {
}
