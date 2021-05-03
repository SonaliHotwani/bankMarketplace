package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;
import lombok.Getter;

@Getter
public class BalanceOperation extends BankOperation {
    private Integer emiNumber;
    private String balanceOutput;

    public BalanceOperation(Command command, BankUser bankUser, Integer emiNumber) {
        super(command, bankUser);
        this.emiNumber = emiNumber;
    }

    @Override
    public void update(BankState bankState) {
        try {
            final Transaction transaction = bankState.getTransactionFor(emiNumber);
            balanceOutput = String.format("%s %s %d %d",
                    getBankUser().getBankName(),
                    getBankUser().getBorrowerName(),
                    transaction.getTotalAmountPaid(),
                    transaction.getNumberOfRemainingEmi());
            System.out.println(balanceOutput);
        } catch (EmiOutOfTenureException e) {
            balanceOutput = String.format("%s %s %s",
                    getBankUser().getBankName(),
                    getBankUser().getBorrowerName(),
                    e.getMessage());
            System.out.println(balanceOutput);
        }
    }
}
