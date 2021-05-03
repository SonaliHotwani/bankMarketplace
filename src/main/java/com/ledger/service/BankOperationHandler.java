package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.BankOperation;
import com.ledger.model.BankState;
import com.ledger.model.BankUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class BankOperationHandler {
    private Map<BankUser, BankState> bankStateForUsers;
    private BankOperationTransformer transformer;

    public BankOperationHandler(BankOperationTransformer transformer) {
        bankStateForUsers = new HashMap<>();
        this.transformer = transformer;
    }

    public void handle(List<String> bankOperationsString) {
        try {
            bankOperationsString.forEach(bankOperation -> {
                BankOperation operation = transformer.getBankOperation(bankOperation);
                setInitialBankState(operation);
                operation.update(getBankStateFor(operation.getBankUser()));
            });
        } catch (InvalidBankOperationException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void setInitialBankState(BankOperation operation) {
        if (Objects.isNull(getBankStateFor(operation.getBankUser())))
            bankStateForUsers.put(operation.getBankUser(), new BankState());
    }

    BankState getBankStateFor(BankUser bankUser) {
        return bankStateForUsers.get(bankUser);
    }
}
