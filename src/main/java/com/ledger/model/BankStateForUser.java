package com.ledger.model;

import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
public class BankStateForUser {

    Integer totalAmountPaid;
    Integer numberOfRemainingEmi;
    Integer remainingAmountToBePaid;
    Integer emiAmount;
    Integer numberOfTotalEmi;

    public BankStateForUser(Integer totalAmountPaid, Integer numberOfRemainingEmi) {
        this.totalAmountPaid = totalAmountPaid;
        this.numberOfRemainingEmi = numberOfRemainingEmi;
    }

    void updateAmountAndRemainingEmi(Integer emiNumber) {
        Integer differenceOfEmiInstallments = numberOfRemainingEmi - (numberOfTotalEmi - emiNumber);
        totalAmountPaid = totalAmountPaid + (differenceOfEmiInstallments * emiAmount);
        remainingAmountToBePaid = remainingAmountToBePaid - totalAmountPaid;
        numberOfRemainingEmi = numberOfTotalEmi - emiNumber;
    }

    void addLumpSumAmount(Integer lumpSumAmount) {
        totalAmountPaid = totalAmountPaid + lumpSumAmount;
        remainingAmountToBePaid = remainingAmountToBePaid - lumpSumAmount;
        Integer remainingEmiAfterLumpSum = new BigDecimal(remainingAmountToBePaid).divide(new BigDecimal(emiAmount), RoundingMode.UP).intValue();
        Integer numberOfEmiPaidDueToLumpSum = numberOfRemainingEmi - remainingEmiAfterLumpSum;
        numberOfTotalEmi = numberOfTotalEmi - numberOfEmiPaidDueToLumpSum;
        numberOfRemainingEmi = remainingEmiAfterLumpSum;
    }

}
