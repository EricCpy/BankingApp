package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.AccountType;
import de.eric.bankingapp.banking.model.BankingAccount;
import de.eric.bankingapp.banking.model.Currency;

import java.util.Date;

public record BankingAccountResponse (
        String IBAN,
        double money,
        double interestRatePA,
        Date creationDate,
        boolean active,
        Currency currency,
        AccountType accountType
) {

    public BankingAccountResponse(BankingAccount bankingAccount) {
        this(bankingAccount.getIBAN(), bankingAccount.getMoney(),
                bankingAccount.getInterestRatePA(), bankingAccount.getCreationDate(), bankingAccount.isActive(),
                bankingAccount.getCurrency(), bankingAccount.getAccountType());
    }
}
