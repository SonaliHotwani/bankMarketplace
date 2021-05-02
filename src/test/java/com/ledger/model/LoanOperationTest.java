package com.ledger.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoanOperationTest {

    @Test
    void shouldUpdateBankStateWithInitialTransactionOfLoanDetails() {
        final LoanDetails loanDetails = new LoanDetails(new BigDecimal("5000"), new BigDecimal("1"), new BigDecimal("6"));
        final LoanOperation loanOperation = new LoanOperation(Command.LOAN, new BankUser("IDIDI", "Dale"), loanDetails);

        final BankStateForUser bankState = mock(BankStateForUser.class);
        loanOperation.update(bankState);

        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(12)
                .numberOfRemainingEmi(12)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(5300)
                .emiAmount(442)
                .build();
        verify(bankState).addInitialTransaction(transaction);
    }
}
