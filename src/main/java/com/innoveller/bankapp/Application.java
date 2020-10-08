package com.innoveller.bankapp;
import java.time.LocalDate;
import java.util.List;
public class Application {
    public static void main(String[] args) {
        BankService service = new BankServiceDB();
        //BankAccount dawMyaAye =service.createAccount("Daw Mya Aye",BankAccountType.DEPOSIT,10000);
        //BankAccount uTunAung =service.createAccount("U Tun Aung",BankAccountType.DEPOSIT,10000);
         //BankAccount dawMyaAye = service.findAccount(1L);
        // BankAccount uTunAung = service.findAccount(2L);
        //service.deposit(dawMyaAye,1000);
        //service.withdraw(dawMyaAye,100000);
        //service.transfer(dawMyaAye,uTunAung,100);

       // service.reportOfDateRange(LocalDate.of(2020,9,1),LocalDate.of(2020,10,));

        service.reportForOneDay(LocalDate.of(2020,9,30));

//        List<Transaction> transactions = service.getAccountTransactionList(uTunAung);
//        System.out.println(transactions);
//        for(Transaction transaction:transactions){
//            System.out.println(transaction.getTransactionType() + " Amount "+transaction.getAmount()+" At Date"+transaction.getTransactionDate());

            // System.out.println(dawMyaAye.balance);
              }
        }

