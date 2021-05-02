package com.ledger.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class Transaction {
    private Integer totalAmountPaid;
    private Integer numberOfRemainingEmi;
    private Integer remainingAmountToBePaid;
    private Integer emiAmount;
    private Integer numberOfTotalEmi;
    private Integer lumpSumAmount;
    private Integer currentEmiNumber;
}
