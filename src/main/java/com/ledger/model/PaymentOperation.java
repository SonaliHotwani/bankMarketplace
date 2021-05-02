package com.ledger.model;

import lombok.Getter;

@Getter
public class PaymentOperation extends BankOperation {
    private Integer lumpSumAmount;
    private Integer currentEmiNumber;

    public PaymentOperation(Command command, BankUser bankUser, Integer lumpSumAmount, Integer emiNumber) {
        super(command, bankUser);
        this.lumpSumAmount = lumpSumAmount;
        this.currentEmiNumber = emiNumber;
    }

    @Override
    public void update(BankStateForUser bankStateForUser) {
        bankStateForUser.addAllTransactionsUpTo(currentEmiNumber, lumpSumAmount);
    }
}
