package com.innoveller.bankapp;

import java.util.List;

public class Application {
    public static void main(String[] args){
        try {
            BankService service = new BankServiceDB();
            //BankAccount DawMyaAye =service.createAccount("Daw Mya Aye",BankAccountType.DEPOSIT,10000);
            //BankAccount UTunAung =service.createAccount("U Tun Aung",BankAccountType.DEPOSIT,10000);
            BankAccount DawMyaAye = service.findAccount(4L);
            BankAccount UTunAung = service.findAccount(5L);
            service.deposit(DawMyaAye,1000);
            service.withdraw(DawMyaAye,100000);
            service.transfer(DawMyaAye,UTunAung,100);

        List<Transaction> transactions = service.getAccountTransaction(DawMyaAye);
        for(Transaction transaction:transactions){
            System.out.println(transaction.getTransactionType() + " Amount "+transaction.getAmount()+" At Date"+transaction.getTransactionDate());
        }
           // System.out.println("Total Amount in the bank =" + DawMyaAye.balance);
        } catch (BankException ex) {
            System.out.println(ex.getMessage());
        }
    }
}