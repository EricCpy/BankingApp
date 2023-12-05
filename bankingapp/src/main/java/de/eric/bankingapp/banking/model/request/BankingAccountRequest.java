package de.eric.bankingapp.banking.model.request;

public record BankingAccountRequest(
        String currency,
        String accountType
) {
}
