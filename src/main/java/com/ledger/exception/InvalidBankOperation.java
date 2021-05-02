package com.ledger.exception;

public class InvalidBankOperation extends RuntimeException {
    public InvalidBankOperation() {
        super("Invalid operation input");
    }
}
