package de.eric.bankingapp.banking.repository;

import de.eric.bankingapp.banking.model.AccountType;
import de.eric.bankingapp.banking.model.AccountTypeInterestRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeInterestRateRepository extends CrudRepository<AccountTypeInterestRate, Long> {
    Optional<AccountTypeInterestRate> findTopByAccountTypeOrderByCreationDateDesc(AccountType accountType);
}
