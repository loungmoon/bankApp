package com.innoveller.bankapp;

import java.sql.SQLException;

public class BankException extends SQLException {
    public BankException(String message,Exception cause) {
        super(message,cause);
    }
}