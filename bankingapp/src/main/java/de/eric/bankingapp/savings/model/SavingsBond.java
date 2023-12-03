package de.eric.bankingapp.savings.model;

import de.eric.bankingapp.banking.model.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class SavingsBond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long savingsBondId;
    long amount;
    double interestRate;
    Currency currency = Currency.EUR;
    Date creationDate;
    Date terminationDate;
}
