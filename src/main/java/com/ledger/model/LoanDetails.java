package com.ledger.model;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;


@AllArgsConstructor
public class LoanDetails {

    private BigDecimal principalAmount;
    private BigDecimal numberOfYears;
    private BigDecimal rateOfInterest;

    private static final String MONTHS_IN_A_YEAR = "12";

    Integer monthlyEmiAmount() {

        BigDecimal totalAmount = principalAmount.add(interest());

        BigDecimal numberOfPayableMonths = numberOfYears.multiply(new BigDecimal(MONTHS_IN_A_YEAR));

        return totalAmount.divide(numberOfPayableMonths, RoundingMode.UP).intValue();
    }

    private BigDecimal interest() {
        return principalAmount.multiply(rateOfInterest).multiply(numberOfYears).divide(new BigDecimal("100"), RoundingMode.UP);
    }

    Integer numberOfEmis() {
        return numberOfYears.multiply(new BigDecimal(MONTHS_IN_A_YEAR)).intValue();
    }

    Integer totalAmount() {
        return principalAmount.add(interest()).intValue();
    }
}
