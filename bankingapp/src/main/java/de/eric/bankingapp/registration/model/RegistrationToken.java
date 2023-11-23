package de.eric.bankingapp.registration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegistrationToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @NaturalId
        String token;
        @NaturalId
        String email;
        Date expiration;
}
