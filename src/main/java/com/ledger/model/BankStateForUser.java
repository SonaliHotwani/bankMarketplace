package com.ledger.model;

import com.ledger.exception.EmiOutOfTenureException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BankStateForUser {

    private Integer totalAmountPaid;
    private Integer numberOfRemainingEmi;
    private Integer remainingAmountToBePaid;
    private Integer emiAmount;
    private Integer numberOfTotalEmi;
    private List<Transaction> transactions;

    public BankStateForUser() {
        transactions = new ArrayList<>();
    }

    void addInitialTransaction(Transaction transaction) {
        transactions.add(transaction);
        numberOfTotalEmi = transaction.getNumberOfTotalEmi();
        numberOfRemainingEmi = transaction.getNumberOfRemainingEmi();
        totalAmountPaid = transaction.getTotalAmountPaid();
        remainingAmountToBePaid = transaction.getRemainingAmountToBePaid();
        emiAmount = transaction.getEmiAmount();
    }

    void addAllTransactionsUpTo(Transaction transaction) {
        final Transaction lastTransaction = transactions.stream().max(Comparator.comparingInt(Transaction::getCurrentEmiNumber)).get();
        for (int i = lastTransaction.getCurrentEmiNumber() + 1; i < transaction.getCurrentEmiNumber(); i++) {
            addTransaction(i, 0);
        }
        addTransaction(transaction.getCurrentEmiNumber(), transaction.getLumpSumAmount());
    }

    Transaction getTransactionFor(Integer emiNumber) throws EmiOutOfTenureException {
        final Optional<Transaction> transactionForEmiNumber = findTransaction(emiNumber);
        if (transactionForEmiNumber.isPresent()) return transactionForEmiNumber.get();
        else {
            addAllTransactionsUpTo(Transaction.builder().currentEmiNumber(emiNumber).build());
            final Optional<Transaction> transaction = findTransaction(emiNumber);
            if (transaction.isPresent())
                return transaction.get();
            else throw new EmiOutOfTenureException(emiNumber);
        }
    }

    private Optional<Transaction> findTransaction(Integer emiNumber) {
        return transactions.stream().filter(transaction -> transaction.getCurrentEmiNumber().equals(emiNumber)).findFirst();
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
