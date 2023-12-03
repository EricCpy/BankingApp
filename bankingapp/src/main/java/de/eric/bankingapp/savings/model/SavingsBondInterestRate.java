package de.eric.bankingapp.savings.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class SavingsBondInterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long savingsBondInterestRateId;
    Date creationDate = new Date();
    double interestRate;
}
