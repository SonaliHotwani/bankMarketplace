package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankStateTest {

    @Test
    void shouldUpdateBankStateWithInitialTransaction() {
        final BankState bankState = new BankState();
        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(12)
                .numberOfRemainingEmi(12)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(5300)
                .emiAmount(442)
                .build();

        bankState.setInitialStateWith(transaction);


        assertEquals(1, bankState.getTransactions().size());
        assertEquals(transaction, bankState.getTransactions().get(0));
    }

    @Test
    void shouldUpdateBankStateWithAllPreviousTransactionsGivenCurrentTransaction() throws EmiOutOfTenureException {
        final BankState bankState = new BankState();
        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(4)
                .numberOfRemainingEmi(4)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(400)
                .emiAmount(100)
                .build();

        bankState.setInitialStateWith(transaction);

        bankState.addAllTransactionsUpTo(2, 100);

        Integer initialTransaction = 1;
        Integer transactionsOfPayment = 2;
        final int expectedTransactions = initialTransaction + transactionsOfPayment;
        assertEquals(expectedTransactions, bankState.getTransactions().size());
        final Transaction transactionForEmi1 = Transaction.builder()
                .totalAmountPaid(100)
                .remainingAmountToBePaid(300)
                .currentEmiNumber(1)
                .numberOfTotalEmi(4)
                .numberOfRemainingEmi(3)
                .emiAmount(100)
                .lumpSumAmount(0)
                .build();
        final Transaction transactionForEmi2 = Transaction.builder()
                .totalAmountPaid(300)
                .remainingAmountToBePaid(100)
                .currentEmiNumber(2)
                .numberOfTotalEmi(3)
                .numberOfRemainingEmi(1)
                .emiAmount(100)
                .lumpSumAmount(100).build();
        assertEquals(transactionForEmi1, bankState.getTransactionFor(1));
        assertEquals(transactionForEmi2, bankState.getTransactionFor(2));
    }

    @Test
    void shouldUpdateLastEmiAmountIfLumpSumIsPaid() throws EmiOutOfTenureException {
        final BankState bankState = new BankState();
        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(4)
                .numberOfRemainingEmi(4)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(400)
                .emiAmount(100)
                .build();

        bankState.setInitialStateWith(transaction);
        bankState.addAllTransactionsUpTo(2, 120);

        final Transaction transactionForEmi3 = Transaction.builder()
                .totalAmountPaid(400)
                .remainingAmountToBePaid(0)
                .currentEmiNumber(3)
                .numberOfTotalEmi(3)
                .numberOfRemainingEmi(0)
                .emiAmount(100)
                .lumpSumAmount(0).build();
        assertEquals(transactionForEmi3, bankState.getTransactionFor(3));
    }

    @Test
    void shouldThrowEmiOutOfTenureExceptionForOutOfRangeEmi() {
        final BankState bankState = new BankState();
        final Transaction transaction = Transaction.builder()
                .numberOfTotalEmi(4)
                .numberOfRemainingEmi(4)
                .totalAmountPaid(0)
                .remainingAmountToBePaid(400)
                .emiAmount(100)
                .build();

        bankState.setInitialStateWith(transaction);

        assertThrows(EmiOutOfTenureException.class, () -> bankState.getTransactionFor(5));
    }

}
