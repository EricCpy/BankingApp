package de.eric.bankingapp.user.model;

import jakarta.persistence.*;

@Entity
public record Account(
        @Id
        Long userId,
        String email,
        String firstName,
        String lastName,
        String password,
        UserRole role,
        boolean emailVerified) {
}
