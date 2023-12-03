package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.AccountType;

import java.util.Currency;
import java.util.Date;

public record BankingAccountResponse (
        String IBAN,
        String BIC,
        double money,
        double interestRatePA,
        Date creationDate,
        boolean active,
        Currency currency,
        AccountType accountType
) {
}
