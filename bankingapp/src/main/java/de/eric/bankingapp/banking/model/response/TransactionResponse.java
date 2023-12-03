package de.eric.bankingapp.banking.model.response;

import java.util.Date;

public record TransactionResponse (
        Date creationDate,
        double amount,
        String description,
        boolean sending,
        String receiver_IBAN,
        String receiver_BIC
) {
}
