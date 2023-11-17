package de.eric.bankingapp.user.repository;

import de.eric.bankingapp.user.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
