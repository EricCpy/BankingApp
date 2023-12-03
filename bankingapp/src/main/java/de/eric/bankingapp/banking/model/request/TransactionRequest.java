package de.eric.bankingapp.banking.model.request;

public record TransactionRequest (
        String description,
        double amount,
        String receiver_IBAN,
        String receiver_BIC
) {
}
