package de.eric.bankingapp.banking.model.request;

public record TransactionRequest(
        String description,
        double amount,
        String receiverIban,
        String receiverBic
) {
}
