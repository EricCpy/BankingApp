package de.eric.bankingapp.banking.model.response;

import de.eric.bankingapp.banking.model.Transaction;

import java.util.Date;

public record TransactionResponse (
        Date creationDate,
        double amount,
        String description,
        boolean sending,
        String receiver_IBAN,
        String receiver_BIC
) {
    public TransactionResponse(Transaction transaction) {
        this(transaction.getCreationTime(), transaction.getAmount(), transaction.getDescription(),
                transaction.isSending(), transaction.getReceiver_IBAN(), transaction.getReceiver_BIC());
    }

}
