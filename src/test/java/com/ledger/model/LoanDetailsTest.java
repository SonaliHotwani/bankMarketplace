package com.ledger.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanDetailsTest {

    @Test
    void shouldReturnMonthlyEmiAmount() {
        final LoanDetails loanDetails = new LoanDetails(new BigDecimal("5000"), new BigDecimal("1"), new BigDecimal("6"));
        assertEquals(442, loanDetails.monthlyEmiAmount());
    }

    @Test
    void shouldReturnNumberOfMonthlyEmis() {
        final LoanDetails loanDetails = new LoanDetails(new BigDecimal("5000"), new BigDecimal("1"), new BigDecimal("6"));
        assertEquals(12, loanDetails.numberOfEmis());
    }

    @Test
    void shouldReturnTotalAmountAddingPrincipalAndInterest() {
        final LoanDetails loanDetails = new LoanDetails(new BigDecimal("5000"), new BigDecimal("1"), new BigDecimal("6"));
        assertEquals(5300, loanDetails.totalAmount());
    }

}
