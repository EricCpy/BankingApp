package de.eric.bankingapp.banking.repository;

import de.eric.bankingapp.banking.model.SavingsBond;
import de.eric.bankingapp.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsBondRepository extends CrudRepository<SavingsBond, Long> {
}
