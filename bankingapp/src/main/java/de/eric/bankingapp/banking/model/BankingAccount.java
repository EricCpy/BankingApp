package de.eric.bankingapp.banking.model;


import de.eric.bankingapp.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
public class BankingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;
    @NaturalId
    String IBAN;
    @NaturalId
    String BIC;
    double money;
    double interestRatePA;
    @Builder.Default
    Date creationDate = new Date();
    @Builder.Default
    boolean active = true;
    @Builder.Default
    Currency currency = Currency.EUR;
    @Builder.Default
    AccountType accountType = AccountType.CHECKING_ACCOUNT;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "bankingAccount", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
}
