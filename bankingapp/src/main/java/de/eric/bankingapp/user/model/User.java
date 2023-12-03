package de.eric.bankingapp.user.model;

import de.eric.bankingapp.banking.model.BankingAccount;
import de.eric.bankingapp.savings.model.SavingsBond;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.List;


@Entity
@Table(name="USER_ACCOUNT")
@With
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long userId;
        @NaturalId(mutable = true)
        String email;
        String firstName;
        String lastName;
        String password;
        UserRole role = UserRole.CUSTOMER;
        boolean blocked = false;
        boolean emailVerified = false;

        @OneToMany(mappedBy = "user")
        private List<BankingAccount> bankingAccounts;

        @OneToMany(mappedBy = "user")
        private List<SavingsBond> savingsBonds;
}

