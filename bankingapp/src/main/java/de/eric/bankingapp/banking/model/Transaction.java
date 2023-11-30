package de.eric.bankingapp.banking.model;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name="BANKING_TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionId;
    Date creationTime;
    long amount;
    String description;
    boolean sending;
    @ManyToOne
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

        public TransactionBuilder amount(long amount) {
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

        public Transaction build() {
            return this.transaction;
        }
    }


}
