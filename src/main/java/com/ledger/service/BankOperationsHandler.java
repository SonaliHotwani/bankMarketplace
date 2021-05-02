package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.*;
import org.apache.commons.lang3.EnumUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BankOperationsHandler {
    public void handle(List<String> bankOperationsString) {

        Map<BankUser, BankStateForUser> bankStateForUsers = new HashMap<>();
        final List<BankOperation> bankOperations = bankOperationsString.stream().map(bankOperation -> {
            BankOperation operation = getBankOperation(bankOperation);

            if (Objects.isNull(bankStateForUsers.get(operation.getBankUser())))
                bankStateForUsers.put(operation.getBankUser(), new BankStateForUser());

            return operation;
        }).collect(Collectors.toList());

        handleOperationsPerUser(bankOperations, bankStateForUsers);
    }

    private BankOperation getBankOperation(String bankOperation) {
        final String[] attributes = bankOperation.split(" ");
        if (EnumUtils.isValidEnum(Command.class, attributes[0])) {
            Command command = Command.valueOf(attributes[0]);
            final BankUser bankUser = new BankUser(attributes[1], attributes[2]);
            switch (command) {
                case LOAN:
                    return new LoanOperation(Command.LOAN,
                            bankUser,
                            new LoanDetails(new BigDecimal(attributes[3]), new BigDecimal(attributes[4]), new BigDecimal(attributes[5])));
                case BALANCE:
                    return new BalanceOperation(Command.BALANCE,
                            bankUser,
                            Integer.valueOf(attributes[3]));
                case PAYMENT:
                    return new PaymentOperation(Command.PAYMENT,
                            bankUser,
                            Integer.valueOf(attributes[3]),
                            Integer.valueOf(attributes[4]));
                default:
                    throw new InvalidBankOperationException(attributes[0]);
            }
        } else
            throw new InvalidBankOperationException(attributes[0]);
    }

    private void handleOperationsPerUser(List<BankOperation> operations, Map<BankUser, BankStateForUser> bankStateForUsers) {
        operations.forEach(operation -> operation.update(bankStateForUsers.get(operation.getBankUser())));
    }
}
