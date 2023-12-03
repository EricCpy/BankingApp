package de.eric.bankingapp.banking.repository;

import de.eric.bankingapp.banking.model.BankingAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankingAccountRepository extends CrudRepository<BankingAccount, Long> {
    Optional<BankingAccount> findByIBAN(String iban);
}
