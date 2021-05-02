package com.ledger.model;

public class LoanOperation extends BankOperation {
    private LoanDetails loanDetails;

    public LoanOperation(Command command, BankUser bankUser, LoanDetails loanDetails) {
        super(command, bankUser);
        this.loanDetails = loanDetails;
    }

    @Override
    public void updateBankState(BankStateForUser bankStateForUser) {
        bankStateForUser.setNumberOfRemainingEmi(this.loanDetails.numberOfEmis());
        bankStateForUser.setNumberOfTotalEmi(this.loanDetails.numberOfEmis());
        bankStateForUser.setRemainingAmountToBePaid(this.loanDetails.totalAmount());
        bankStateForUser.setEmiAmount(this.loanDetails.monthlyEmiAmount());
        bankStateForUser.setTotalAmountPaid(0);
    }
}
