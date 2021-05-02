package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.BankOperation;
import com.ledger.model.BankStateForUser;
import com.ledger.model.BankUser;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ledger.service.BankOperationTransformer.getBankOperation;

public class BankOperationHandler {
    @Getter
    private Map<BankUser, BankStateForUser> bankStateForUsers = new HashMap<>();

    public void handle(List<String> bankOperationsString) {
        try {
            bankOperationsString.forEach(bankOperation -> {
                BankOperation operation = getBankOperation(bankOperation);
                setInitialBankState(operation);
                operation.update(getBankStateFor(operation.getBankUser()));
            });
        } catch (InvalidBankOperationException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void setInitialBankState(BankOperation operation) {
        if (Objects.isNull(getBankStateFor(operation.getBankUser())))
            bankStateForUsers.put(operation.getBankUser(), new BankStateForUser());
    }

    private BankStateForUser getBankStateFor(BankUser bankUser) {
        return bankStateForUsers.get(bankUser);
    }
}
