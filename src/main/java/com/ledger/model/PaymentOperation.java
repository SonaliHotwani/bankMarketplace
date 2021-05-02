package com.ledger.model;

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
        final Transaction transaction = Transaction.builder()
                .currentEmiNumber(currentEmiNumber)
                .lumpSumAmount(lumpSumAmount).build();

        bankStateForUser.addAllTransactionsUpTo(transaction);
    }
}
