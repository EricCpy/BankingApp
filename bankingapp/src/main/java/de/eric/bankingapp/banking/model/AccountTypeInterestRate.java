package de.eric.bankingapp.banking.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class AccountTypeInterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountTypeInterestRateId;
    AccountType accountType;
    Date creationDate = new Date();
    double interestRatePA;
}
