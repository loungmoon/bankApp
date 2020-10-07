//package com.innoveller.bankapp;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class BankServiceImplement implements BankService {
//    private Map<Long,BankAccount> bankAccountMap = new HashMap<>();
//    private List<Transaction> transactionList = new ArrayList<>();
//
//    @Override
//    public BankAccount findAccount(Long id) {
//        return bankAccountMap.get(id);
//    }
//
//    @Override
//    public BankAccount createAccount(String accountHolder, BankAccountType accountType, double balance) {
//        Long id = bankAccountMap.size()+1L;
//        int accountNo = (int)(Math.random()*100000000);
//        LocalDate opendate = LocalDate.now(ZoneId.systemDefault());
//        BankAccount account = new BankAccount(id,accountNo,accountHolder,accountType,opendate,balance);
//        Transaction depositTransation = new Transaction(balance,TransactionType.DEPOSIT,account.getId());
//        bankAccountMap.put(account.getId(),account);
//        transactionList.add(depositTransation);
//        return account;
//    }
//
//    @Override
//    public void deposit(BankAccount account, double amount) {
//        Transaction depositTransation = new Transaction(amount,TransactionType.DEPOSIT,account.getId());
//        account.balance  += amount;
//        transactionList.add(depositTransation);
//    }
//
//    @Override
//    public void withdraw(BankAccount account, double amount) {
//        if(account.balance>amount){
//            Transaction withdrawTransaction = new Transaction(amount,TransactionType.WITHDRAW,account.getId());
//            account.balance  -= amount;
//            transactionList.add(withdrawTransaction);
//        }
//        else {
//            System.out.println("Fail Withdraw Transaction");
//
//        }
//    }
//
//    @Override
//    public void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
//        Transaction transferTransaction = new Transaction(amount,TransactionType.TRANSFER,fromAccount.getId());
//        if(fromAccount.balance<amount){
//            System.out.println("Fail Transaction");
//        }
//        else {
//            fromAccount.balance -=amount;
//            toAccount.balance +=amount;
//        }
//        transactionList.add(transferTransaction);
//    }
//
//    @Override
//    public List<Transaction> getAccountTransaction(BankAccount account) {
//        List<Transaction> transactionAccount= new ArrayList<>();
//
//        for(Transaction transaction:transactionList){
//            if(account.getId().equals(transaction.getBankAccountId())){
//                transactionAccount.add(transaction);
//            }
//        }
//        return transactionAccount;
//    }
//}
