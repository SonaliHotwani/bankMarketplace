package com.ledger.exception;

public class EmiOutOfTenureException extends Exception {
    public EmiOutOfTenureException(Integer emiNumber) {
        super("EMI Number: " + emiNumber + " is out of tenure");
    }
}
