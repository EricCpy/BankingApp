package de.eric.bankingapp.banking.model.request;

public record BankingAccountEditRequest(
        String currency,
        Boolean active
) {
}
