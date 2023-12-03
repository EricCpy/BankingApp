package de.eric.bankingapp.savings.repository;

import de.eric.bankingapp.savings.model.SavingsBondInterestRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsBondInterestRateRepository extends CrudRepository<SavingsBondInterestRate, Long> {

}
