package com.innoveller.bankapp;
import java.time.LocalDate;

public class BankAccount {
    private Long id;
    private int AccountNo;
    private String accountHolder;
    private BankAccountType accountType;
    private LocalDate openDate;
    public double balance;

    public BankAccount(Long id, int accountNo, String accountHolder, BankAccountType accountType, LocalDate openDate, double balance) {
        this.id = id;
        AccountNo = accountNo;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.openDate = openDate;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

}
