package de.eric.bankingapp.registration.repository;

import de.eric.bankingapp.registration.model.RegistrationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationToken, Long> {
    Optional<RegistrationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RegistrationToken r WHERE r.email = :email")
    void deleteByEmail(String email);
}
