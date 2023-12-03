package de.eric.bankingapp.savings.repository;

import de.eric.bankingapp.savings.model.SavingsBond;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsBondRepository extends CrudRepository<SavingsBond, Long> {
}
