package de.eric.bankingapp.banking.model;


import de.eric.bankingapp.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
public class BankingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;
    double money;
    double interestRatePA;
    @Builder.Default
    boolean active = true;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @Builder.Default
    Currency currency = Currency.EUR;
    @Builder.Default
    AccountType accountType = AccountType.CHECKING_ACCOUNT;
    @OneToMany(mappedBy = "bankingAccount", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
}
