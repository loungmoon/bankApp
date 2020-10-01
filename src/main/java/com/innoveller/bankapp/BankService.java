package com.innoveller.bankapp;

import java.util.List;

public interface BankService {
    BankAccount findAccount(Long id) throws BankException;
    BankAccount createAccount(String accountHolder, BankAccountType accountType,double balance) throws BankException;
    void deposit(BankAccount account,double amount) throws BankException;
    void withdraw(BankAccount account,double amount) throws BankException;
    void transfer(BankAccount fromAccount,BankAccount toAccount,double amount) throws BankException;
    List<Transaction> getAccountTransaction(BankAccount account) throws BankException;
}
