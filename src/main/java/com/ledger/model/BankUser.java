package com.ledger.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BankUser {
    private final String bankName;
    private final String borrowerName;

    public BankUser(String bankName, String borrowerName) {
        this.bankName = bankName;
        this.borrowerName = borrowerName;
    }
}
