package com.ledger.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
class Transaction {
    private Integer totalAmountPaid;
    private Integer numberOfRemainingEmi;
    private Integer remainingAmountToBePaid;
    private Integer emiAmount;
    private Integer numberOfTotalEmi;
    private Integer lumpSumAmount;
    private Integer currentEmiNumber;
}
