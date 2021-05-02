package com.ledger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BankOperation {
    private Command command;
    private BankUser bankUser;

    public abstract void updateBankState(BankStateForUser bankStateForUser);
}

