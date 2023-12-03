package de.eric.bankingapp.banking.model;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountTypeInterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountTypeInterestRateId;
    AccountType accountType;
    @Builder.Default
    Date creationDate = new Date();
    double interestRatePA;
}
