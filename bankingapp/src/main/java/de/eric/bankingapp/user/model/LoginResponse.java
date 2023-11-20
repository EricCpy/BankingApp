package de.eric.bankingapp.user.model;

import java.util.Date;

public record LoginResponse(
        String email,
        String token
) {
}
