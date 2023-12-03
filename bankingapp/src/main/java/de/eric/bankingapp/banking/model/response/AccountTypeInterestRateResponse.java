package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.AccountType;
import de.eric.bankingapp.banking.model.AccountTypeInterestRate;

import java.util.Date;

public record AccountTypeInterestRateResponse(
        AccountType accountType,
        Date creationDate,
        double interestRatePA
) {

    public AccountTypeInterestRateResponse(AccountTypeInterestRate accountTypeInterestRate) {
        this(accountTypeInterestRate.getAccountType(), accountTypeInterestRate.getCreationDate(),
                accountTypeInterestRate.getInterestRatePA());
    }
}
