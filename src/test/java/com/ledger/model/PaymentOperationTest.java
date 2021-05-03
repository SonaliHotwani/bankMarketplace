package com.ledger.model;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PaymentOperationTest {

    @Test
    void shouldUpdateBankStateWithAllTransactionsTillCurrentEmiTransaction() {
        final PaymentOperation paymentOperation = new PaymentOperation(Command.PAYMENT, new BankUser("IDIDI", "Dale"), 100, 5);
        final BankState bankState = mock(BankState.class);

        paymentOperation.update(bankState);

        verify(bankState).addAllTransactionsUpTo(5, 100);
    }

}
