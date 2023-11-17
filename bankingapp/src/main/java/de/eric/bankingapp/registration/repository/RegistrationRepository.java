package de.eric.bankingapp.registration.repository;

import de.eric.bankingapp.registration.model.RegistrationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationToken, Long> {
    Optional<RegistrationToken> findByToken(String token);
}
