package de.eric.bankingapp.registration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public record RegistrationToken (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        long id,
        String token,
        Date expirationTime

){

}
