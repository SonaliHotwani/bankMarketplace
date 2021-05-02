package com.ledger.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankStateForUserTest {

    @Test
    void shouldUpdateBankStateWithInitialTransaction() {
        final BankStateForUser bankState = new BankStateForUser();
        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(12)
                .numberOfRemainingEmi(12)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(5300)
                .emiAmount(442)
                .currentEmiNumber(0)
                .build();

        bankState.addInitialTransaction(transaction);


        assertEquals(1, bankState.getTransactions().size());
        assertEquals(transaction, bankState.getTransactions().get(0));
    }
}
