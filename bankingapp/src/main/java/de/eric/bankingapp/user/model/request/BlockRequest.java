package de.eric.bankingapp.user.model.request;

import lombok.NonNull;

public record BlockRequest(
        @NonNull
        String email,
        boolean block
) {
}
