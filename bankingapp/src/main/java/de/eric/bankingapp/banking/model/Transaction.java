package de.eric.bankingapp.banking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Table(name = "BANKING_TRANSACTION")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionId;
    Date creationTime = new Date();
    double amount;
    String description;
    boolean sending;
    String receiverIban;
    String receiverBic;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    BankingAccount bankingAccount;

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    //just for example, because of the task, normally you would use lombok @Builder Pattern
    public static class TransactionBuilder {
        private final Transaction transaction;

        private TransactionBuilder() {
            this.transaction = new Transaction();
        }

        public TransactionBuilder transactionId(Long transactionId) {
            this.transaction.transactionId = transactionId;
            return this;
        }

        public TransactionBuilder creationTime(Date creationTime) {
            this.transaction.creationTime = creationTime;
            return this;
        }

        public TransactionBuilder amount(double amount) {
            this.transaction.amount = amount;
            return this;
        }

        public TransactionBuilder description(String description) {
            this.transaction.description = description;
            return this;
        }

        public TransactionBuilder sending(boolean sending) {
            this.transaction.sending = sending;
            return this;
        }

        public TransactionBuilder bankingAccount(BankingAccount bankingAccount) {
            this.transaction.bankingAccount = bankingAccount;
            return this;
        }


        public TransactionBuilder receiverIban(String receiverIban) {
            this.transaction.receiverIban = receiverIban;
            return this;
        }

        public TransactionBuilder receiverBic(String receiverBic) {
            this.transaction.receiverBic = receiverBic;
            return this;
        }

        public Transaction build() {
            return this.transaction;
        }
    }


}
