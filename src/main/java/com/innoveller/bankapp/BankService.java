package com.innoveller.bankapp;
import java.time.LocalDate;
import java.util.List;

public interface BankService {
    BankAccount findAccount(Long id);
    BankAccount createAccount(String accountHolder, BankAccountType accountType,double balance);
    void deposit(BankAccount account,double amount);
    void withdraw(BankAccount account,double amount);
    void transfer(BankAccount fromAccount,BankAccount toAccount,double amount);
    List<Transaction> getAccountTransactionList(BankAccount account);
    List<Transaction> reportOfDateRange(LocalDate from_date,LocalDate to_date);
    List<Transaction> reportForOneDay(LocalDate date);

}
