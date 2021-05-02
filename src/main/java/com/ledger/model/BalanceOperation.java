package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;

public class BalanceOperation extends BankOperation {
    private Integer emiNumber;

    public BalanceOperation(Command command, BankUser bankUser, Integer emiNumber) {
        super(command, bankUser);
        this.emiNumber = emiNumber;
    }

    @Override
    public void update(BankStateForUser bankStateForUser) {
        try {
            final Transaction transaction = bankStateForUser.getTransactionFor(emiNumber);
            System.out.println(
                    String.format("%s %s %d %d",
                            getBankUser().getBankName(),
                            getBankUser().getBorrowerName(),
                            transaction.getTotalAmountPaid(),
                            transaction.getNumberOfRemainingEmi()));
        } catch (EmiOutOfTenureException e) {
            System.out.println(
                    String.format("%s %s %s",
                            getBankUser().getBankName(),
                            getBankUser().getBorrowerName(),
                            e.getMessage()));

        }
    }
}
