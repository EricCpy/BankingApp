package de.eric.bankingapp.banking.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Builder
public class AccountTypeInterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountTypeInterestRateId;
    AccountType accountType;
    @Builder.Default
    LocalDate creationDate = LocalDate.now();
    double interestRatePA;
}
