package com.ledger.exception;

public class InvalidBankOperationException extends RuntimeException {
    public InvalidBankOperationException(String operation) {
        super("Operation: " + operation + " not supported");
    }
}
