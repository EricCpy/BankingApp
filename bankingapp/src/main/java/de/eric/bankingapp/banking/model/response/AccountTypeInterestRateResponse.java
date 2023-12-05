package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.AccountType;
import de.eric.bankingapp.banking.model.AccountTypeInterestRate;

import java.time.LocalDate;

public record AccountTypeInterestRateResponse(
        AccountType accountType,
        LocalDate creationDate,
        double interestRatePA
) {

    public AccountTypeInterestRateResponse(AccountTypeInterestRate accountTypeInterestRate) {
        this(accountTypeInterestRate.getAccountType(), accountTypeInterestRate.getCreationDate(),
                accountTypeInterestRate.getInterestRatePA());
    }
}
