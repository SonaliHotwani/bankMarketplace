package com.ledger.model;

public class BalanceOperation extends BankOperation {
    private Integer emiNumber;

    public BalanceOperation(Command command, BankUser bankUser, Integer emiNumber) {
        super(command, bankUser);
        this.emiNumber = emiNumber;
    }

    @Override
    public void updateBankState(BankStateForUser bankStateForUser) {
        bankStateForUser.updateAmountAndRemainingEmi(emiNumber);
        System.out.println(
                String.format("%s %s %d %d",
                        getBankUser().getBankName(),
                        getBankUser().getBorrowerName(),
                        bankStateForUser.totalAmountPaid,
                        bankStateForUser.numberOfRemainingEmi));
    }
}
