package com.ledger.model;

public class LoanOperation extends BankOperation {
    private LoanDetails loanDetails;

    public LoanOperation(Command command, BankUser bankUser, LoanDetails loanDetails) {
        super(command, bankUser);
        this.loanDetails = loanDetails;
    }

    @Override
    public void update(BankStateForUser bankStateForUser) {
        final Integer numberOfRemainingEmi = this.loanDetails.numberOfEmis();
        final Transaction transaction = Transaction.builder().emiAmount(this.loanDetails.monthlyEmiAmount())
                .numberOfTotalEmi(numberOfRemainingEmi)
                .numberOfRemainingEmi(numberOfRemainingEmi)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(this.loanDetails.totalAmount())
                .currentEmiNumber(0)
                .build();
        bankStateForUser.addInitialTransaction(transaction);
    }
}
