package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BankStateForUser {

    private Integer totalAmountPaid;
    private Integer numberOfRemainingEmi;
    private Integer remainingAmountToBePaid;
    private Integer emiAmount;
    private Integer numberOfTotalEmi;
    @Getter
    private List<Transaction> transactions;

    public BankStateForUser() {
        transactions = new ArrayList<>();
    }

    void setInitialStateWith(Transaction transaction) {
        transactions = new ArrayList<>();
        transactions.add(transaction);
        numberOfTotalEmi = transaction.getNumberOfTotalEmi();
        numberOfRemainingEmi = transaction.getNumberOfRemainingEmi();
        totalAmountPaid = transaction.getTotalAmountPaid();
        remainingAmountToBePaid = transaction.getRemainingAmountToBePaid();
        emiAmount = transaction.getEmiAmount();
    }

    void addAllTransactionsUpTo(Integer emiNumber, Integer lumpSumAmount) {
        Integer previousEmiNumber = 0;
        final Optional<Transaction> lastTransaction = transactions.stream()
                .filter(transaction -> transaction.getCurrentEmiNumber() != null)
                .max(Comparator.comparingInt(Transaction::getCurrentEmiNumber));
        if (lastTransaction.isPresent()) previousEmiNumber = lastTransaction.get().getCurrentEmiNumber();
        for (int i = previousEmiNumber + 1; i < emiNumber; i++) {
            addTransaction(i, 0);
        }
        addTransaction(emiNumber, lumpSumAmount);
    }

    Transaction getTransactionFor(Integer emiNumber) throws EmiOutOfTenureException {
        final Optional<Transaction> transactionForEmiNumber = findTransaction(emiNumber);
        if (transactionForEmiNumber.isPresent()) return transactionForEmiNumber.get();
        else {
            addAllTransactionsUpTo(emiNumber, 0);
            final Optional<Transaction> transaction = findTransaction(emiNumber);
            if (transaction.isPresent())
                return transaction.get();
            else throw new EmiOutOfTenureException(emiNumber);
        }
    }

    private Optional<Transaction> findTransaction(Integer emiNumber) {
        return transactions.stream().filter(transaction -> Objects.nonNull(transaction.getCurrentEmiNumber()) && transaction.getCurrentEmiNumber().equals(emiNumber)).findFirst();
    }

    private void addTransaction(Integer emiNumber, Integer lumpSumAmount) {
        if (numberOfRemainingEmi != 0) {
            updateAmountAndRemainingEmi(emiNumber);
            if (Objects.nonNull(lumpSumAmount) && lumpSumAmount > 0) addLumpSumAmount(lumpSumAmount);
            final Transaction transaction = Transaction.builder()
                    .currentEmiNumber(emiNumber)
                    .emiAmount(emiAmount)
                    .numberOfRemainingEmi(numberOfRemainingEmi)
                    .numberOfTotalEmi(numberOfTotalEmi)
                    .remainingAmountToBePaid(remainingAmountToBePaid)
                    .totalAmountPaid(totalAmountPaid)
                    .lumpSumAmount(lumpSumAmount)
                    .build();
            transactions.add(transaction);
        }
    }

    private void updateAmountAndRemainingEmi(Integer emiNumber) {
        if (remainingAmountToBePaid < emiAmount) {
            totalAmountPaid = totalAmountPaid + remainingAmountToBePaid;
            remainingAmountToBePaid = 0;
            numberOfRemainingEmi = 0;
        }
        Integer differenceOfEmiInstallments = numberOfRemainingEmi - (numberOfTotalEmi - emiNumber);
        totalAmountPaid = totalAmountPaid + (differenceOfEmiInstallments * emiAmount);
        remainingAmountToBePaid = remainingAmountToBePaid - (differenceOfEmiInstallments * emiAmount);
        numberOfRemainingEmi = numberOfTotalEmi - emiNumber;
    }

    private void addLumpSumAmount(Integer lumpSumAmount) {
        totalAmountPaid = totalAmountPaid + lumpSumAmount;
        remainingAmountToBePaid = remainingAmountToBePaid - lumpSumAmount;
        Integer remainingEmiAfterLumpSum = new BigDecimal(remainingAmountToBePaid).divide(new BigDecimal(emiAmount), RoundingMode.UP).intValue();
        Integer numberOfEmiPaidDueToLumpSum = numberOfRemainingEmi - remainingEmiAfterLumpSum;
        numberOfTotalEmi = numberOfTotalEmi - numberOfEmiPaidDueToLumpSum;
        numberOfRemainingEmi = remainingEmiAfterLumpSum;
    }
}
