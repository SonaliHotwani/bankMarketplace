package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class BankOperationHandlerTest {

    @Test
    void shouldHandleOperationAndUpdateBankState() {
        final BankOperationTransformer transformer = mock(BankOperationTransformer.class);
        final BankOperationHandler handler = new BankOperationHandler(transformer);
        List<String> bankOperationsString = Arrays.asList(
                "LOAN IDIDI Dale 5000 1 6",
                "PAYMENT IDIDI Dale 1000 5",
                "BALANCE IDIDI Dale 3");
        final LoanOperation loanOperation = mock(LoanOperation.class);
        final PaymentOperation paymentOperation = mock(PaymentOperation.class);
        final BalanceOperation balanceOperation = mock(BalanceOperation.class);

        final BankUser bankUser = new BankUser("IDIDI", "Dale");
        when(loanOperation.getBankUser()).thenReturn(bankUser);
        when(paymentOperation.getBankUser()).thenReturn(bankUser);
        when(balanceOperation.getBankUser()).thenReturn(bankUser);

        when(transformer.getBankOperation("LOAN IDIDI Dale 5000 1 6")).thenReturn(loanOperation);
        when(transformer.getBankOperation("PAYMENT IDIDI Dale 1000 5")).thenReturn(paymentOperation);
        when(transformer.getBankOperation("BALANCE IDIDI Dale 3")).thenReturn(balanceOperation);

        handler.handle(bankOperationsString);


        verify(loanOperation).update(handler.getBankStateFor(bankUser));
        verify(paymentOperation).update(handler.getBankStateFor(bankUser));
        verify(balanceOperation).update(handler.getBankStateFor(bankUser));
    }

    @Test
    void shouldNotUpdateBankStateForInvalidBankOperation() {
        final BankOperationTransformer transformer = mock(BankOperationTransformer.class);
        final BankOperationHandler handler = new BankOperationHandler(transformer);
        List<String> bankOperationsString = Collections.singletonList(
                "VIEW IDIDI Dale 5000 1 6");
        final BankOperation invalidOperation = mock(BankOperation.class);
        final BankUser bankUser = new BankUser("IDIDI", "Dale");

        final InvalidBankOperationException exception = new InvalidBankOperationException("Operation: " + bankOperationsString.get(0) + " not supported");
        when(transformer.getBankOperation("VIEW IDIDI Dale 5000 1 6")).thenThrow(exception);

        handler.handle(bankOperationsString);
        verify(invalidOperation, times(0)).update(any());
    }
}
