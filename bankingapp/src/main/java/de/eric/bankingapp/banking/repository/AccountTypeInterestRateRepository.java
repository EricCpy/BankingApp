package de.eric.bankingapp.banking.repository;

import de.eric.bankingapp.banking.model.AccountTypeInterestRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeInterestRateRepository extends CrudRepository<AccountTypeInterestRate, Long> {

}
