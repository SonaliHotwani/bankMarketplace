package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BalanceOperationTest {

    @Test
    void shouldReturnDetailsOfAmountPaidAndRemainingEmi() throws EmiOutOfTenureException {
        final BalanceOperation balanceOperation = new BalanceOperation(Command.BALANCE, new BankUser("IDIDI", "Dale"), 5);

        final BankState bankState = mock(BankState.class);
        when(bankState.getTransactionFor(5)).thenReturn(Transaction.builder().totalAmountPaid(1000).numberOfRemainingEmi(2).build());
        balanceOperation.update(bankState);

        verify(bankState).getTransactionFor(5);
        assertEquals("IDIDI Dale 1000 2", balanceOperation.getBalanceOutput());
    }

    @Test
    void shouldReturnErrorMessageForEmiNumberWhichIsOutOfTenure() throws EmiOutOfTenureException {
        final BalanceOperation balanceOperation = new BalanceOperation(Command.BALANCE, new BankUser("IDIDI", "Dale"), 5);

        final BankState bankState = mock(BankState.class);
        when(bankState.getTransactionFor(5)).thenThrow(new EmiOutOfTenureException(5));
        balanceOperation.update(bankState);

        assertThrows(EmiOutOfTenureException.class, () -> bankState.getTransactionFor(5));
        assertEquals("IDIDI Dale EMI Number: 5 is out of tenure", balanceOperation.getBalanceOutput());
    }

}
