package com.innoveller.bankapp;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
public class BankServiceDB implements BankService {
    Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank_application_db", "postgres", "innoveller");
        } catch (SQLException e) {
            System.out.println("connection error");
        }
    }

    public void saveTransaction(double amount, TransactionType transactionType, Long bankAccountId) {
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        String sql = "INSERT INTO transaction(transaction_date,amount,transaction_type,bank_account_id) VALUES (?,?,?,?)";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(date));
            statement.setDouble(2, amount);
            statement.setString(3, String.valueOf(transactionType));
            statement.setLong(4, bankAccountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new BankException("Sql Transaction Statement Error", e);
            } catch (BankException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public double calculatedTotalBalance(Long bank_account_id) {
        List<Transaction> transactionList = new ArrayList<>();
        Statement stmt;
        double totalAmount = 0.0;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from transaction where transaction.bank_account_id=" + bank_account_id);

            while (rs.next()) {
                TransactionType transactionType;
                if (rs.getString(4).equals(TransactionType.DEPOSIT.toString())) {
                    transactionType = TransactionType.DEPOSIT;
                } else if (rs.getString(4).equals(TransactionType.WITHDRAW.toString())) {
                    transactionType = TransactionType.WITHDRAW;
                } else {
                    transactionType = TransactionType.TRANSFER;
                }
                Transaction transaction = new Transaction(rs.getDouble(3), transactionType, rs.getLong(5));
                transactionList.add(transaction);

            }

            for (Transaction transaction : transactionList) {
                if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
                    totalAmount += transaction.getAmount();
                } else if (transaction.getTransactionType().equals(TransactionType.WITHDRAW)) {
                    totalAmount -= transaction.getAmount();
                } else {
                    totalAmount -= transaction.getAmount();
                }
            }

        } catch (SQLException ex) {
            try {
                throw new BankException("Error in Sql statement in calculate balance", ex);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }
        return totalAmount;
    }


    @Override
    public BankAccount findAccount(Long id) {
        BankAccount bankAccount = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bank_account where bank_account.id=" + id);
            rs.next();
            BankAccountType bankAccountType;
            if (rs.getString(4).equals(BankAccountType.DEPOSIT)) {
                bankAccountType = BankAccountType.DEPOSIT;
            } else {
                bankAccountType = BankAccountType.SAVING;
            }
            double balance = calculatedTotalBalance(rs.getLong(1));
            bankAccount = new BankAccount(rs.getLong(1), rs.getInt(2), rs.getString(3), bankAccountType, LocalDate.parse(rs.getString(5)), balance);
        } catch (SQLException e) {
            try {
                throw new BankException("Sql statement error for find this account", e);
            } catch (BankException e1) {
                System.out.println(e1.getMessage());
            }
        }
        return bankAccount;
    }

    @Override
    public BankAccount createAccount(String accountHolder, BankAccountType accountType, double balance) {
        int accountNo = (int) (Math.random() * 100000000);
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        String sql = "INSERT INTO bank_account(account_no,account_holder,account_type,open_date) VALUES (?,?,?,?)";
        BankAccount bankAccount = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNo);
            statement.setString(2, accountHolder);
            statement.setString(3, String.valueOf(accountType));
            statement.setDate(4, Date.valueOf(date));
            statement.executeUpdate();
            statement.close();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bank_account where bank_account.account_no =" + accountNo);
            rs.next();
            BankAccountType bankAccountType;
            if (rs.getString(4).equals(BankAccountType.DEPOSIT.toString())) {
                bankAccountType = BankAccountType.DEPOSIT;
            } else {
                bankAccountType = BankAccountType.SAVING;
            }
            Long bank_account_id = rs.getLong(1);
            saveTransaction(balance, TransactionType.DEPOSIT, bank_account_id);
            double balanceAmount = calculatedTotalBalance(bank_account_id);
            bankAccount = new BankAccount(rs.getLong(1), rs.getInt(2), rs.getString(3), bankAccountType, LocalDate.parse(rs.getString(5)), balanceAmount);
        } catch (SQLException e) {
            try {
                throw new BankException("Sql statement error for create this account", e);
            } catch (BankException e1) {
                System.out.println(e1.getMessage());
            }
        }
        return bankAccount;
    }

    @Override
    public void deposit(BankAccount account, double amount) {
        saveTransaction(amount, TransactionType.DEPOSIT, account.getId());
    }

    @Override
    public void withdraw(BankAccount account, double amount) {
        try {
            double totalBalance = calculatedTotalBalance(account.getId());
            if (totalBalance > amount) {
                saveTransaction(amount, TransactionType.WITHDRAW, account.getId());
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Withdraw fail at amount  " + amount);
        }
    }

    @Override
    public void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        try {
            double totalAmount = calculatedTotalBalance(fromAccount.getId());
            if (totalAmount > amount) {
                saveTransaction(amount, TransactionType.TRANSFER, fromAccount.getId());
                saveTransaction(amount, TransactionType.DEPOSIT, toAccount.getId());
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Cannot Transfer");
        }
    }

    @Override
    public List<Transaction> getAccountTransactionList(BankAccount account) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from transaction where transaction.bank_account_id=" + account.getId());

            while (rs.next()) {
                TransactionType transactionType;
                if (rs.getString(4).equals(TransactionType.DEPOSIT.toString())) {
                    transactionType = TransactionType.DEPOSIT;
                } else if (rs.getString(4).equals(TransactionType.WITHDRAW.toString())) {
                    transactionType = TransactionType.WITHDRAW;
                } else {
                    transactionType = TransactionType.TRANSFER;
                }
                Transaction transaction = new Transaction(rs.getDouble(3), transactionType, rs.getLong(5));
                transactionList.add(transaction);
            }
        } catch (SQLException ex) {
            try {
                throw new BankException("sql Statement Error at get list transaction", ex);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }
        return transactionList;
    }

    @Override
    public void reportOfDateRange(LocalDate from_date,LocalDate to_date) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from transaction where transaction_date between '"+from_date+"' and '"+to_date+"'");
            while (rs.next()) {
                TransactionType transactionType;
                if (rs.getString(4).equals(TransactionType.DEPOSIT.toString())) {
                    transactionType = TransactionType.DEPOSIT;
                } else if (rs.getString(4).equals(TransactionType.WITHDRAW.toString())) {
                    transactionType = TransactionType.WITHDRAW;
                } else {
                    transactionType = TransactionType.TRANSFER;
                }
                Transaction transaction = new Transaction(rs.getDouble(3), transactionType, rs.getLong(5));
                transactionList.add(transaction);
            }
        } catch (SQLException ex) {
            try {
                throw new BankException("sql Statement Error at get list transaction", ex);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }
       for(Transaction transaction:transactionList){
           System.out.println("Transaction Type :"+transaction.getTransactionType() + " Amount : " + transaction.getAmount()+" Bank_Account_ID: "+transaction.getBankAccountId());
       }
    }

    @Override
    public void reportForOneDay(LocalDate date) {
        List<Transaction> transactionList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from transaction where transaction_date = '"+ date+"'");
            while (rs.next()) {
                TransactionType transactionType;
                if (rs.getString(4).equals(TransactionType.DEPOSIT.toString())) {
                    transactionType = TransactionType.DEPOSIT;
                } else if (rs.getString(4).equals(TransactionType.WITHDRAW.toString())) {
                    transactionType = TransactionType.WITHDRAW;
                } else {
                    transactionType = TransactionType.TRANSFER;
                }
                Transaction transaction = new Transaction(rs.getDouble(3), transactionType, rs.getLong(5));
                transactionList.add(transaction);
            }
        } catch (SQLException ex) {
            try {
                throw new BankException("sql Statement Error at get list transaction", ex);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }
        for(Transaction transaction:transactionList){
            System.out.println("Transaction Type :"+transaction.getTransactionType() + " Amount : " + transaction.getAmount()+" Bank_Account_ID: "+transaction.getBankAccountId());
        }
    }
    }
