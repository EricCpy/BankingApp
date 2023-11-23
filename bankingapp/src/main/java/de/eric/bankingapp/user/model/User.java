package de.eric.bankingapp.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;


@Entity
@Table(name="Account")
@With
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long userId;
        @NaturalId(mutable = true)
        String email;
        String firstName;
        String lastName;
        String password;
        UserRole role = UserRole.ROLE_CUSTOMER;
        boolean emailVerified = false;
}

