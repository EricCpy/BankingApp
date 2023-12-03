package de.eric.bankingapp.banking.model.request;

import de.eric.bankingapp.banking.model.Currency;

public record BankingAccountEditRequest(
        String currency,
        Boolean active
) {
}
