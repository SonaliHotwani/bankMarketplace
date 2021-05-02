package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankOperationTransformerTest {

    private BankOperationTransformer transformer = new BankOperationTransformer();

    @Test
    void shouldReturnLoanOperationInstanceFromLoanCommandString() {
        final String loanCommandString = "LOAN IDIDI Dale 5000 1 6";
        final LoanDetails loanDetails = new LoanDetails(
                new BigDecimal("5000"),
                new BigDecimal("1"),
                new BigDecimal("6")
        );
        final BankUser bankUser = new BankUser("IDIDI", "Dale");

        final BankOperation bankOperation = transformer.getBankOperation(loanCommandString);

        assertTrue(bankOperation instanceof LoanOperation);
        assertEquals(bankUser, bankOperation.getBankUser());
        assertEquals(loanDetails, ((LoanOperation) bankOperation).getLoanDetails());
    }

    @Test
    void shouldThrowExceptionIfLoanAttributesAreMissingInTheCommand() {
        final String loanCommandString = "LOAN IDIDI Dale 5000 1";
        assertThrows(InvalidBankOperationException.class, () -> transformer.getBankOperation(loanCommandString));
    }

    @Test
    void shouldReturnPaymentOperationInstanceFromPaymentCommandString() {
        final String paymentCommandString = "PAYMENT IDIDI Dale 1000 5";

        final BankUser bankUser = new BankUser("IDIDI", "Dale");

        final BankOperation bankOperation = transformer.getBankOperation(paymentCommandString);

        assertTrue(bankOperation instanceof PaymentOperation);
        assertEquals(bankUser, bankOperation.getBankUser());
        assertEquals(1000, ((PaymentOperation) bankOperation).getLumpSumAmount());
        assertEquals(5, ((PaymentOperation) bankOperation).getCurrentEmiNumber());
    }

    @Test
    void shouldThrowExceptionIfPaymentAttributesAreMissingInTheCommand() {
        final String paymentCommandString = "PAYMENT IDIDI Dale 1000";
        assertThrows(InvalidBankOperationException.class, () -> transformer.getBankOperation(paymentCommandString));
    }

    @Test
    void shouldReturnBalanceOperationInstanceFromBalanceCommandString() {
        final String balanceCommandString = "BALANCE IDIDI Dale 3";

        final BankUser bankUser = new BankUser("IDIDI", "Dale");

        final BankOperation bankOperation = transformer.getBankOperation(balanceCommandString);

        assertTrue(bankOperation instanceof BalanceOperation);
        assertEquals(bankUser, bankOperation.getBankUser());
        assertEquals(3, ((BalanceOperation) bankOperation).getEmiNumber());
    }

    @Test
    void shouldThrowExceptionIfBalanceAttributesAreMissingInTheCommand() {
        final String balanceCommandString = "BALANCE IDIDI Dale";
        assertThrows(InvalidBankOperationException.class, () -> transformer.getBankOperation(balanceCommandString));
    }

    @Test
    void shouldThrowExceptionForAnyOtherCommandBesidesLoanPaymentAndBalance() {
        final String otherCommandString = "VIEW IDIDI Dale 1";
        assertThrows(InvalidBankOperationException.class, () -> transformer.getBankOperation(otherCommandString));
    }

    @Test
    void shouldThrowExceptionIfBorrowerNameIsMissing() {
        final String otherCommandString = "BALANCE IDIDI ";
        assertThrows(InvalidBankOperationException.class, () -> transformer.getBankOperation(otherCommandString));
    }
}
