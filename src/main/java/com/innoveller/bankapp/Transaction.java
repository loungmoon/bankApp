package com.innoveller.bankapp;

import java.time.LocalDate;
import java.time.ZoneId;

public class Transaction {
    private LocalDate transactionDate;
    private double amount ;
    private TransactionType transactionType;
    private Long bankAccountId;

    public Transaction( double amount, TransactionType transactionType, Long bankAccountId) {
        this.transactionDate =  LocalDate.now(ZoneId.systemDefault());
        this.amount = amount;
        this.transactionType = transactionType;
        this.bankAccountId = bankAccountId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

}
