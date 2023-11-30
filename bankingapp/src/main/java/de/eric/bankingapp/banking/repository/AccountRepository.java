package de.eric.bankingapp.banking.repository;

import de.eric.bankingapp.banking.model.BankingAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<BankingAccount, Long> {

}
