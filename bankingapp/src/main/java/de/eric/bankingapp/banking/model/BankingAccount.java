package de.eric.bankingapp.banking.model;


import de.eric.bankingapp.user.model.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class BankingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;
    double money;
    double interestRatePA;
    Date creationDate;
    boolean active;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    Currency currency = Currency.EUR;
    AccountType accountType = AccountType.CHECKING_ACCOUNT;
    @OneToMany(mappedBy = "bankingAccount", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
