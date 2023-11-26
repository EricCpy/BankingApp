package de.eric.bankingapp.banking.model;

import jakarta.persistence.*;

import java.util.Date;

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionId;
    Date creationTime;
    long amount;
    String description;
    boolean sending;
    @ManyToOne
    @JoinColumn(name = "account_id")
    BankingAccount bankingAccount;
}
