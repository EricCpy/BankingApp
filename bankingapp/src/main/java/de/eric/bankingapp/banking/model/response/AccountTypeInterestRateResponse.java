package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.AccountType;

import java.util.Date;

public record AccountTypeInterestRateResponse(
        AccountType accountType,
        Date creationDate,
        double interestRatePA
) {
}
