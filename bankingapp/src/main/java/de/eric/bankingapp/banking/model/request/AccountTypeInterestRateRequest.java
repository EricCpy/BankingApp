package de.eric.bankingapp.banking.model.request;

public record AccountTypeInterestRateRequest(
        String accountType,
        double interestRatePA
) {
}
